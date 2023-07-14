package com.cn.app.superbot.exception;

/**
 * The type Conversation exception.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class ConversationException extends Exception {

    /**
     * Instantiates a new Conversation exception.
     *
     * @param message the message
     */
    public ConversationException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Conversation exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public ConversationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
