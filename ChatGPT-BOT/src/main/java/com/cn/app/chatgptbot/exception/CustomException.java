package com.cn.app.chatgptbot.exception;


/**
 * custom exceptions
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@SuppressWarnings("all")
public class CustomException extends RuntimeException {

    /*
    customMessages
     */
    private String message;


    /**
     * Instantiates a new Custom exception.
     *
     * @param message the message
     */
    public CustomException(final String message) {
        super(message);
        this.message = message;

    }
}
