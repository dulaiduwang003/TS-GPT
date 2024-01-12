package com.cn.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.constant.OrdersStatusConstant;
import com.cn.dto.DeleteExceptionDto;
import com.cn.entity.TsException;
import com.cn.entity.TsOrders;
import com.cn.entity.TsUser;
import com.cn.mapper.TsExceptionMapper;
import com.cn.mapper.TsOrdersMapper;
import com.cn.mapper.TsUserMapper;
import com.cn.service.AhiService;
import com.cn.utils.RedisUtils;
import com.cn.vo.ExceptionPageVo;
import com.cn.vo.SiteStatisticsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.cn.constant.StatisticsConstant.*;

/**
 * 终端 业务层 实现
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
@Slf4j
public class AhiServiceImpl implements AhiService {

    private final RedisUtils redisUtils;

    private final TsOrdersMapper tsOrdersMapper;

    private final TsUserMapper tsUserMapper;

    private final TsExceptionMapper tsExceptionMapper;


    @Override
    public void deleteException(DeleteExceptionDto dto) {
        tsExceptionMapper.delete(new QueryWrapper<TsException>()
                .lambda()
                .eq(TsException::getExceptionId, dto.getExceptionId())
        );
    }

    @Override
    public IPage<ExceptionPageVo> getExceptionPage(int pageNum) {
        final Page<TsException> tsExceptionPage = tsExceptionMapper.selectPage(new Page<>(pageNum, 15), new QueryWrapper<TsException>()
                .lambda()
                .orderByDesc(TsException::getCreatedTime)
        );
        System.out.println(tsExceptionPage.getRecords());
      return   tsExceptionPage.convert(t -> {
            return new ExceptionPageVo()
                    .setExceptionId(t.getExceptionId())
                    .setCreatedTime(t.getCreatedTime())
                    .setUpdateTime(t.getUpdateTime())
                    .setLevel(t.getLevel())
                    .setServerName(t.getServerName())
                    .setCause(t.getCause());
        });
    }

    @Override
    public SiteStatisticsVo getSiteStatistics() {
        final Double newRevenue = (Double) redisUtils.getValue(NEW_REVENUE);
        final Integer newTrade = (Integer) redisUtils.getValue(NEW_TRADE);
        final Integer newUsers = (Integer) redisUtils.getValue(NEW_USERS);
        final Integer newVisits = (Integer) redisUtils.getValue(NEW_VISITS);
        final Integer totalVisits = (Integer) redisUtils.getValue(TOTAL_VISITS);
        //总用户
        final Long totalUsers = tsUserMapper.selectCount(new QueryWrapper<TsUser>());
        //总收入
        final Double totalRevenue = tsOrdersMapper.sumSucceedOrdersPrice();
        //总交易订单
        final Long totalTrade = tsOrdersMapper.selectCount(new QueryWrapper<TsOrders>()
                .lambda()
                .eq(TsOrders::getStatus, OrdersStatusConstant.SUCCEED)
        );
        //获取总访问量
        return new SiteStatisticsVo()
                .setNewRevenue(newRevenue)
                .setNewTrade(newTrade)
                .setNewUsers(newUsers)
                .setNewVisits(newVisits)
                .setTotalRevenue(totalRevenue)
                .setTotalTrade(totalTrade)
                .setTotalVisits(totalVisits)
                .setTotalUsers(totalUsers);
    }
}
