package com.cn.service;

import com.cn.dto.CreatedPayAlipayDto;
import com.cn.vo.OrdersPayCodeVo;
import com.cn.vo.UserProductCardVo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 商品支付下单 业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface ProductPayService {

    /**
     * 获取所有商品信息
     *
     * @return the all product card
     */
    List<UserProductCardVo> getAllProductCard();


    /**
     * 获取订单支付状态
     *
     * @param ordersId the orders id
     * @return the ali pay status
     */
    Boolean getOrdersPayStatus(final String ordersId);


    /**
     *  创建支付宝订单
     *
     * @param dto the dto
     * @return the alipay pay code vo
     */
    OrdersPayCodeVo createdAliPayOrders(final CreatedPayAlipayDto dto) ;


    /**
     * 支付宝回调
     *
     * @param request the request
     * @return the string
     */
    String alipayPullback(final HttpServletRequest request);
}
