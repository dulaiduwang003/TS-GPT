package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付订单视图
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class OrdersPayCodeVo implements Serializable {
    /**
     * 订单ID
     */
    private String ordersId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 订单创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 交易价格
     */
    private Double price;

    /**
     * 支付二维码
     */
    private String qrCode;


}
