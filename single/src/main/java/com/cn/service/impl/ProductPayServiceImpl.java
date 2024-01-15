package com.cn.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.constant.OrdersStatusConstant;
import com.cn.constant.UserCacheConstant;
import com.cn.dto.CreatedPayAlipayDto;
import com.cn.entity.TsOrders;
import com.cn.entity.TsProductCard;
import com.cn.entity.TsUser;
import com.cn.exception.OrdersException;
import com.cn.mapper.TsOrdersMapper;
import com.cn.mapper.TsProductCardMapper;
import com.cn.mapper.TsUserMapper;
import com.cn.queue.UnpaidOrderQueue;
import com.cn.service.ProductPayService;
import com.cn.structure.AlipayCacheStructure;
import com.cn.structure.UserInfoStructure;
import com.cn.utils.*;
import com.cn.vo.OrdersPayCodeVo;
import com.cn.vo.UserProductCardVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cn.constant.OrdersConstant.*;
import static com.cn.constant.OrdersStatusConstant.WAIT;
import static com.cn.constant.StatisticsConstant.NEW_REVENUE;
import static com.cn.constant.StatisticsConstant.NEW_TRADE;


/**
 * 商品支付下单 业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductPayServiceImpl implements ProductPayService {

    private final TsUserMapper tsUserMapper;

    private final TsProductCardMapper tsProductCardMapper;

    private final RedisLockHelper redisLockHelper;

    private final IdGeneratorUtils idGeneratorUtils;

    private final TsOrdersMapper tsOrdersMapper;

    private final RedisUtils redisUtils;

    private final UnpaidOrderQueue unpaidOrderQueue;

    @Value("${ali-pay.appId}")
    private String appId;

    @Value("${ali-pay.alipayPublicKey}")
    private String alipayPublicKey;

    @Value("${ali-pay.privateKey}")
    private String privateKey;

    @Value("${ali-pay.domain}")
    private String domain;

    @Override
    public OrdersPayCodeVo createdAliPayOrders(final CreatedPayAlipayDto dto) {
        final String timestamp = String.valueOf(System.currentTimeMillis());
        //当前登录用户ID
        final Long currentLoginId = UserUtils.getCurrentLoginId();
        //锁前缀
        final String lockPrefix = ORDER_CREATED_LOCK + currentLoginId;
        //上锁
        final boolean lock = redisLockHelper.lock(lockPrefix, timestamp);


        if (!lock) {
            throw new OrdersException("操作繁忙,请勿重复下单", 500);
        }
        final String QR_CACHE = ORDER_QRCODE_CACHE + currentLoginId + dto.getProductCardId();
        try {
            if (redisUtils.doesItExist(QR_CACHE)) {
                final AlipayCacheStructure cache = (AlipayCacheStructure) redisUtils.getValue(QR_CACHE);
                //生成BASE64图片给前端
                return new OrdersPayCodeVo()
                        .setOrdersId(cache.getOrdersId())
                        .setQrCode(QRCodeGenerator.generateQRCode(cache.getUrl()))
                        .setPrice(cache.getPrice())
                        .setProductName(cache.getProductName())
                        .setCreatedTime(cache.getCreatedTime());
            }

            final TsProductCard tsProductCard = tsProductCardMapper.selectOne(new QueryWrapper<TsProductCard>().lambda().eq(TsProductCard::getProductCardId, dto.getProductCardId()).select(TsProductCard::getProductName, TsProductCard::getDays, TsProductCard::getPrice));

            if (tsProductCard == null) {
                throw new OrdersException("商品不存在或已被下架");
            }
            final String ordersId = idGeneratorUtils.getOrderNo();

            final TsOrders tsOrders = new TsOrders().setOrdersId(ordersId).setPrice(tsProductCard.getPrice()).setStatus(WAIT).setDays(tsProductCard.getDays()).setProductName(tsProductCard.getProductName()).setUserId(currentLoginId);
            tsOrdersMapper.insert(tsOrders);
            //装载配置
            final AlipayConfig alipayConfig = new AlipayConfig();
            alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
            alipayConfig.setFormat("json");
            alipayConfig.setCharset("UTF8");
            alipayConfig.setSignType("RSA2");
            alipayConfig.setAppId(appId);
            alipayConfig.setAlipayPublicKey(alipayPublicKey);
            alipayConfig.setPrivateKey(privateKey);

            //构建支付宝订单
            final AlipayTradePrecreateResponse response = getAlipayTradePrecreateResponse(alipayConfig, ordersId, tsProductCard);
            if (response.isSuccess()) {
                final AlipayCacheStructure cache = new AlipayCacheStructure()
                        .setCreatedTime(tsOrders.getCreatedTime())
                        .setProductName(tsProductCard.getProductName())
                        .setUrl(response.getQrCode())
                        .setPrice(tsProductCard.getPrice())
                        .setOrdersId(ordersId);
                //缓存订单数据
                redisUtils.setValueTimeout(QR_CACHE, cache, 300);
                //添加至 待支付 队列中
                unpaidOrderQueue.add(ordersId);
                redisUtils.setValueTimeout(ORDER_PAY_STATUS + ordersId, false, 600);
                //返回base64编码支付二维码图片
                return new OrdersPayCodeVo().setOrdersId(ordersId).setQrCode(QRCodeGenerator.generateQRCode(response.getQrCode())).setPrice(tsProductCard.getPrice()).setProductName(tsProductCard.getProductName()).setCreatedTime(LocalDateTime.now());
            }
            throw new RuntimeException();
        } catch (Exception e) {
            log.error("生成支付宝支付二维码失败:{}", e.getMessage());
            throw new OrdersException("创建支付二维码失败,请稍后重试");
        } finally {
            redisLockHelper.unlock(lockPrefix, timestamp);
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String alipayPullback(final HttpServletRequest request) {
        final Map<String, String> params = getStringMap(request);
        // 调用SDK验证签名
        boolean signVerified;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF8", "RSA2");
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        // 验证成功
        if (signVerified) {
            String tradeStatus = request.getParameter("trade_status");
            log.info("回调结果:{}", tradeStatus);
            // 支付成功
            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                final String outTradeNo = request.getParameter("out_trade_no");
                final TsOrders orders = tsOrdersMapper
                        .selectOne(new QueryWrapper<TsOrders>()
                                .lambda().eq(TsOrders::getOrdersId, outTradeNo)
                                .select(TsOrders::getOrdersId, TsOrders::getDays, TsOrders::getUserId, TsOrders::getPrice));
                if (orders != null) {
                    tsOrdersMapper.updateById(orders.setOrdersId(outTradeNo)
                            //已支付
                            .setStatus(OrdersStatusConstant.SUCCEED));
                    //数据采集(订单)
                    redisUtils.increment(NEW_TRADE, 1);
                    redisUtils.increment(NEW_REVENUE, orders.getPrice());

                    redisUtils.setValueTimeout(ORDER_PAY_STATUS + outTradeNo, true, 600);
                    //设置用户存期
                    final TsUser tsUser = tsUserMapper
                            .selectOne(new QueryWrapper<TsUser>()
                                    .lambda()
                                    .eq(TsUser::getUserId, orders.getUserId())
                                    .select(TsUser::getUserId,
                                            TsUser::getNickName,
                                            TsUser::getExpirationTime,
                                            TsUser::getEmail,
                                            TsUser::getType,
                                            TsUser::getAvatar,
                                            TsUser::getOpenId));
                    LocalDateTime expirationTime;
                    //系统当前时间
                    final LocalDateTime now = LocalDateTime.now();
                    if (tsUser.getExpirationTime() == null) {
                        expirationTime = now;
                    } else {
                        expirationTime = tsUser.getExpirationTime();
                        //比较日期
                        final LocalDate a = expirationTime.toLocalDate();
                        final LocalDate b = now.toLocalDate();
                        if (a.isBefore(b) || a.isEqual(b)) {
                            // 早于系统当前时间了 重置
                            expirationTime = now;
                        }
                    }
                    expirationTime = expirationTime.plus(Period.ofDays(Math.toIntExact(orders.getDays())));
                    tsUserMapper.updateById(tsUser.setExpirationTime(expirationTime));
                    //更新用户缓存
                    StpUtil.getSessionByLoginId(tsUser.getUserId())
                            .set(UserCacheConstant.USER_INFO_DATA, new UserInfoStructure()
                                    .setNickName(tsUser.getNickName())
                                    .setExpirationTime(tsUser.getExpirationTime())
                                    .setEmail(tsUser.getEmail())
                                    .setType(tsUser.getType())
                                    .setAvatar(tsUser.getAvatar())
                                    .setOpenId(tsUser.getOpenId()));

                }
                return "success";
            }
        } else {
            log.error("支付失败");
            return "fail";
        }
        return "fail";
    }

    /**
     * 转化支付宝入参
     *
     * @param request the request
     * @return the string map
     */
    @NotNull
    private static Map<String, String> getStringMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            StringBuilder valueStr = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                valueStr.append((i == values.length - 1) ? values[i] : values[i] + ",");
            }
            params.put(name, valueStr.toString());
        }
        return params;
    }

    /**
     * Gets alipay trade recreate response.
     *
     * @param alipayConfig  the alipay config
     * @param ordersId      the orders id
     * @param tsProductCard the ts product card
     * @return the alipay trade recreate response
     * @throws AlipayApiException the alipay api exception
     */
    private AlipayTradePrecreateResponse getAlipayTradePrecreateResponse(AlipayConfig alipayConfig, String ordersId, TsProductCard tsProductCard) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
        //预构建请求
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setOutTradeNo(ordersId);
        //支付金额
        model.setTotalAmount(String.valueOf(tsProductCard.getPrice()));
        //商品名称
        model.setSubject(tsProductCard.getProductName());
        //5分钟过期
        model.setTimeoutExpress("5m");
        request.setBizModel(model);
        //支付宝回调地址
        request.setNotifyUrl(domain + "/revelation/alipay/callback");
        return alipayClient.execute(request);
    }

    @Override
    public Boolean getOrdersPayStatus(final String ordersId) {
        final Object value = redisUtils.getValue(ORDER_PAY_STATUS + ordersId);
        if (value == null) {
            throw new OrdersException("长时间未支付,订单已被取消");
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    @Override
    public List<UserProductCardVo> getAllProductCard() {
        return tsProductCardMapper.selectList(new QueryWrapper<TsProductCard>().lambda().select(TsProductCard::getProductCardId, TsProductCard::getDays, TsProductCard::getPrice, TsProductCard::getProductName)).stream().map(s -> new UserProductCardVo().setProductCardId(s.getProductCardId()).setProductName(s.getProductName()).setPrice(s.getPrice()).setDays(s.getDays())).toList();

    }

}
