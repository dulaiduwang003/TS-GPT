package com.cn.exceptions;


import lombok.Data;


/**
 * The type Login exception.
 */
@SuppressWarnings("all")
@Data
public class MemberException extends RuntimeException {

    /**
     * The Message.
     */
    private String message;

    /**
     * The Code.
     */
    private Integer code;


    /**
     * Instantiates a new Member exception.
     *
     * @param message the message
     * @param code    the code
     */
    public MemberException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public MemberException(final String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }

}
