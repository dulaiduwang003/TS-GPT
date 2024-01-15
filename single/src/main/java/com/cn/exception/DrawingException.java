package com.cn.exception;

import lombok.Data;

/**
 * The type Drawing exception.
 */
@SuppressWarnings("all")
@Data
public class DrawingException extends RuntimeException {

    /**
     * The Message.
     */
    private String message;

    /**
     * The Code.
     */
    private Integer code;


    /**
     * Instantiates a new Drawing exception.
     *
     * @param message the message
     * @param code    the code
     */
    public DrawingException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public DrawingException(final String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }
}
