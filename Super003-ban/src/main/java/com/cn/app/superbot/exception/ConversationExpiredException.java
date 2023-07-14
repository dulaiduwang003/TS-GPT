package com.cn.app.superbot.exception;

/**
 * The type Conversation expired exception.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class ConversationExpiredException extends ConversationException {
    /**
     * Instantiates a new Conversation expired exception.
     *
     */
    public ConversationExpiredException() {
        super("Conversation has expired");
    }
}
