package com.cn.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 订单表
 *
 * @author 欧渐风 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "ts_orders")
@Accessors(chain = true)
public class TsOrders {

    /**
     * 订单号
     */
    @TableId(type = IdType.INPUT)
    private String ordersId;

    /**
     * 所属用户
     */
    private Long userId;


    /**
     * 商品名称
     */
    private String productName;

    /**
     * 交易金额
     */
    private Double price;

    /**
     * 商品所含天数
     */
    private Long days;

    /**
     * 订单状态
     */
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
