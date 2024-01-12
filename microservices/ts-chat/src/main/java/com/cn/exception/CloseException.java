package com.cn.exception;


import lombok.Data;


/**
 * 关闭链接异常
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
@Data
public class CloseException extends RuntimeException {

    private String message;

    private Integer code;


    public CloseException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public CloseException() {

        this.message = message;
        this.code = 500;
    }

}
