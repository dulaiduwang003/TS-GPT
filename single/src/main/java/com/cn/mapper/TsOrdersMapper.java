package com.cn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.entity.TsOrders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 订单 mapper
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Mapper
public interface TsOrdersMapper extends BaseMapper<TsOrders> {

    /**
     * 获取订单表中 成功交易的订单总和
     *
     * @return the double
     */
    @Select("SELECT SUM(price) FROM ts_orders where `status` = 'SUCCEED'")
    Double sumSucceedOrdersPrice();
}
