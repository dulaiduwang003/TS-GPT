package com.cn.exception;


import lombok.Data;


/**
 * 用户异常
 *
 * @author 欧渐风 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
@Data
public class UserException extends RuntimeException {

    private String message;

    private Integer code;


    public UserException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public UserException(final String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }

}
