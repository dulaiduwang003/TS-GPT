package com.cn.exception;


import lombok.Data;


/**
 * 对话异常
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
@Data
public class ChatException extends RuntimeException {

    private String message;

    private Integer code = 500;


    public ChatException(final String message) {
        super(message);
        this.message = message;

    }

    public ChatException() {

        this.message = message;
        this.code = 500;
    }

}
