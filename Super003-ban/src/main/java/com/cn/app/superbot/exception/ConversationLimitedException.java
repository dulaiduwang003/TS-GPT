package com.cn.app.superbot.exception;

/**
 * The type Conversation limited exception.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class ConversationLimitedException extends ConversationException {
    /**
     * Instantiates a new Conversation limited exception.
     */
    public ConversationLimitedException() {
        super("Conversation is limited");
    }
}
