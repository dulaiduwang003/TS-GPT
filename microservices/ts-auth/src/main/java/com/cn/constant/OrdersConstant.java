package com.cn.constant;

/**
 * 订单 Constant
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
public interface OrdersConstant {

    /**
     * 订单队列
     */
    String ORDERS_QUEUE = "ORDERS_QUEUE";

    /**
     * 订单锁定
     */
    String ORDER_CREATED_LOCK = "ORDER_CREATED_LOCK:USER:";

    /**
     * 订单状态
     */
    String ORDER_PAY_STATUS = "ORDER_PAY_STATUS:ORDER_ID:";

    /**
     * 支付二维码未过期缓存
     */
    String ORDER_QRCODE_CACHE = "ORDER_QRCODE_CACHE:";
}
