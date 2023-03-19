package com.cn.app.chatgptbot.exception;


import lombok.extern.slf4j.Slf4j;

/**
 * custom exceptions
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@SuppressWarnings("all")
@Slf4j
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
    public CustomException(final String message, Exception e) {
        super(message);
        this.message = message;
        log.error("Bing请求失败：" + e.getMessage(), e);
    }

    public CustomException(final String message) {
        super(message);
        this.message = message;

    }
}
