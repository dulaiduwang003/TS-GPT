package com.cn.exception;

import lombok.Data;

/**
 * The type Dall exception.
 */
@SuppressWarnings("all")
@Data
public class DallException extends RuntimeException {

    /**
     * The Message.
     */
    private String message;

    /**
     * The Code.
     */
    private Integer code;


    /**
     * Instantiates a new Dall exception.
     *
     * @param message the message
     * @param code    the code
     */
    public DallException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    /**
     * Instantiates a new Dall exception.
     *
     * @param message the message
     */
    public DallException(final String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }
}
