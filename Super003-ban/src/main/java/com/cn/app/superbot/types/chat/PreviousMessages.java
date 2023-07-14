package com.cn.app.superbot.types.chat;

/**
 * The type Previous messages.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class PreviousMessages {
    /**
     * The Text.
     */
    private final String text;
    /**
     * The Author.
     */
    private final String author = "bot";
    /**
     * The Adaptive cards.
     */
    private final String[] adaptiveCards = {};
    /**
     * The Suggested responses.
     */
    private final SuggestedResponses[] suggestedResponses;
    /**
     * The Message id.
     */
    private final String messageId;
    /**
     * The Message type.
     */
    private final String messageType;

    /**
     * Instantiates a new Previous messages.
     *
     * @param text               the text
     * @param author             the author
     * @param adaptiveCards      the adaptive cards
     * @param suggestedResponses the suggested responses
     * @param messageId          the message id
     * @param messageType        the message type
     */
    public PreviousMessages(String text, String author, String[] adaptiveCards, SuggestedResponses[] suggestedResponses, String messageId, String messageType) {
        this.text = text;
        this.suggestedResponses = suggestedResponses;
        this.messageId = messageId;
        this.messageType = messageType;
    }
}
