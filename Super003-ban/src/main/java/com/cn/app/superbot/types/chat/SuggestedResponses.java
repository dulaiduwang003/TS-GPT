package com.cn.app.superbot.types.chat;

/**
 * The type Suggested responses.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class SuggestedResponses {
    /**
     * The Text.
     */
    private final String text;
    /**
     * The Content origin.
     */
    private final String contentOrigin;
    /**
     * The Message type.
     */
    private final String messageType;
    /**
     * The Message id.
     */
    private final String messageId;
    /**
     * The Offense.
     */
    private final String offense;

    /**
     * Instantiates a new Suggested responses.
     *
     * @param text          the text
     * @param contentOrigin the content origin
     * @param messageType   the message type
     * @param messageId     the message id
     * @param offense       the offense

     */
    public SuggestedResponses(String text, String contentOrigin, String messageType, String messageId, String offense) {
        this.text = text;
        this.contentOrigin = contentOrigin;
        this.messageType = messageType;
        this.messageId = messageId;
        this.offense = offense;
    }
}
