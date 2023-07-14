package com.cn.app.superbot.types.chat;

/**
 * The type Argument.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class Argument {
    /**
     * The Source.
     */
    private final String source = "cib";
    /**
     * The Options sets.
     */
    private final String[] optionsSets = {
            "nlu_direct_response_filter",
            "deepleo",
            "disable_emoji_spoken_text",
            "responsible_ai_policy_235",
            "enablemm",
            "galileo",
            "wlthrottle",
            "blocklistv2",
            "disbing",
            "dv3sugg"
    };
    /**
     * The Allowed message types.
     */
    private final String[] allowedMessageTypes = {
            "Chat",
            "InternalSearchQuery",
            "InternalSearchResult",
            "Disengaged",
            "InternalLoaderMessage",
            "RenderCardRequest",
            "AdsQuery",
            "SemanticSerp",
            "GenerateContentQuery",
            "SearchQuery"
    };
    /**
     * The Slice ids.
     */
    private final String[] sliceIds = {
            "revdv3cf",
            "perfinstcf",
            "0310wlthrot",
            "302blocklist",
            "308disbing",
            "314glprompts0"
    };
    /**
     * The Trace id.
     */
    private final String traceId;
    /**
     * The Is start of session.
     */
    private final boolean isStartOfSession;
    /**
     * The Message.
     */
    private final Message message;
    /**
     * The Conversation signature.
     */
    private final String conversationSignature;
    /**
     * The Participant.
     */
    private final Participant participant;
    /**
     * The Conversation id.
     */
    private final String conversationId;
    /**
     * The Previous messages.
     */
    private final PreviousMessages[] previousMessages;

    /**
     * Instantiates a new Argument.
     *
     * @param traceId               the trace id
     * @param isStartOfSession      the is start of session
     * @param message               the message
     * @param conversationSignature the conversation signature
     * @param participant           the participant
     * @param conversationId        the conversation id
     * @param previousMessages      the previous messages
     */
    public Argument(String traceId, boolean isStartOfSession, Message message, String conversationSignature, Participant participant, String conversationId, PreviousMessages[] previousMessages) {
        this.traceId = traceId;
        this.isStartOfSession = isStartOfSession;
        this.message = message;
        this.conversationSignature = conversationSignature;
        this.participant = participant;
        this.conversationId = conversationId;
        this.previousMessages = previousMessages;
    }
}
