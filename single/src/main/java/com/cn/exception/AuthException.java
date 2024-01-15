package com.cn.exception;


import lombok.Data;


/**
 * 鉴权异常
 *
 * @author 欧渐风 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
@Data
public class AuthException extends RuntimeException {

    private String message;

    private Integer code;


    public AuthException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public AuthException(final String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }

}
