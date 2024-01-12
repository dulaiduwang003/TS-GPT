package com.cn.exception;


import lombok.Data;


/**
 * 登录异常
 *
 * @author 欧渐风 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
@Data
public class LoginException extends RuntimeException {

    private String message;

    private Integer code;


    public LoginException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public LoginException(final String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }

}
