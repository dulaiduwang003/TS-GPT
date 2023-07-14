package com.cn.app.superbot.types;


import com.cn.app.superbot.types.chat.Argument;

/**
 * The type Chat websocket json.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class ChatWebsocketJson {
    /**
     * The Arguments.
     */
    private final Argument[] arguments;
    /**
     * The Invocation id.
     */
    private final String invocationId;
    /**
     * The Target.
     */
    private final String target = "chat";
    /**
     * The Type.
     */
    private final int type = 4;

    /**
     * Instantiates a new Chat websocket json.
     *
     * @param args         the args
     * @param invocationId the invocation id
     */
    public ChatWebsocketJson(Argument[] args, String invocationId) {
        arguments = args;
        this.invocationId = invocationId;
    }

    /**
     * Get arguments argument [ ].
     *
     * @return the argument [ ]
     */
    public Argument[] getArguments() {
        return arguments;
    }

    /**
     * Gets invocation id.
     *
     * @return the invocation id
     */
    public String getInvocationId() {
        return invocationId;
    }
}
