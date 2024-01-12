package com.cn.exception;


import lombok.Data;


/**
 * 订单异常
 *
 * @author 欧渐风 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
@Data
public class OrdersException extends RuntimeException {

    private String message;

    private Integer code;


    public OrdersException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public OrdersException(final String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }

}
