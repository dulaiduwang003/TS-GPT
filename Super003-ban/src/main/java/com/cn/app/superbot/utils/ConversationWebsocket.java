package com.cn.app.superbot.utils;

import com.cn.app.superbot.interfaces.Callback;
import com.cn.app.superbot.interfaces.Logger;
import com.cn.app.superbot.types.ChatWebsocketJson;
import com.cn.app.superbot.types.chat.Argument;
import com.cn.app.superbot.types.chat.Message;
import com.cn.app.superbot.types.chat.Participant;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The type Conversation websocket.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class ConversationWebsocket extends WebSocketListener {
    /**
     * The Terminal char.
     */
    private final char TerminalChar = '\u001e';
    /**
     * The Conversation id.
     */
    private final String conversationId;
    /**
     * The Client id.
     */
    private final String clientId;
    /**
     * The Conversation signature.
     */
    private final String conversationSignature;
    /**
     * The Question.
     */
    private final String question;
    /**
     * The Invocation id.
     */
    private final String invocationID;
    /**
     * The Callback.
     */
    private final Callback callback;
    /**
     * The Logger.
     */
    private final Logger logger;
    /**
     * The Locale.
     */
    private final String locale;

    /**
     * Instantiates a new Conversation websocket.
     *
     * @param ConversationId        the conversation id
     * @param ClientId              the client id
     * @param ConversationSignature the conversation signature
     * @param question              the question
     * @param invocationID          the invocation id
     * @param callback              the callback
     * @param logger                the logger
     * @param locale                the locale
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public ConversationWebsocket(String ConversationId, String ClientId, String ConversationSignature, String question, short invocationID, Callback callback, Logger logger, String locale) {
        conversationId = ConversationId;
        clientId = ClientId;
        conversationSignature = ConversationSignature;
        this.question = question;
        this.invocationID = String.valueOf(invocationID);
        this.callback = callback;
        this.logger = logger;
        this.locale = locale;
    }

    /**
     * On closed.
     *
     * @param webSocket the web socket
     * @param code      the code
     * @param reason    the reason
     */
    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        logger.Info(String.format("[%s] [%s] websocket is closed", conversationSignature, question));
        super.onClosed(webSocket, code, reason);
    }

    /**
     * On closing.
     *
     * @param webSocket the web socket
     * @param code      the code
     * @param reason    the reason
     */
    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        logger.Info(String.format("[%s] [%s] websocket is closing", conversationSignature, question));
        super.onClosing(webSocket, code, reason);
    }

    /**
     * On failure.
     *
     * @param webSocket the web socket
     * @param t         the t
     * @param response  the response
     */
    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        //logger.Error(String.format("[%s] [%s] websocket is failed, reason: [%s]",conversationSignature,question, t.getCause()));
        webSocket.close(1000, String.valueOf(TerminalChar));
        super.onFailure(webSocket, t, response);
    }

    /**
     * On message.
     *
     * @param webSocket the web socket
     * @param text      the text
     */
    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        for (String textSpited : text.split(String.valueOf(TerminalChar))) {
            logger.Debug(String.format("[%s] [%s] websocket is received new message [%s]", conversationSignature, question, textSpited));
            JsonObject json = JsonParser.parseString(textSpited).getAsJsonObject();
            if (json.isEmpty()) {
                sendData(webSocket, "{\"type\":6}");
                String s = new GsonBuilder().disableHtmlEscaping().create().toJson(
                        new ChatWebsocketJson(new Argument[]{
                                new Argument(
                                        BingUtils.randomString(32).toLowerCase(),
                                        invocationID.equals("0"),
                                        new Message(
                                                locale,
                                                locale,
                                                null,
                                                null,
                                                null,
                                                BingUtils.getNowTime(),
                                                question
                                        ),
                                        conversationSignature,
                                        new Participant(clientId),
                                        conversationId,
                                        null
                                )
                        }, invocationID)
                );
                sendData(webSocket, s);
            } else if (json.has("type")) {
                int type = json.getAsJsonPrimitive("type").getAsInt();
                if (type == 3) {
                    //end
                    webSocket.close(1000, String.valueOf(TerminalChar));
                } else if (type == 6) {
                    //resend packet
                    sendData(webSocket, "{\"type\":6}");
                } else if (type == 2) {
                    if (json.getAsJsonObject("item").has("result") && !json.getAsJsonObject("item").getAsJsonObject("result").getAsJsonPrimitive("value").getAsString().equals("Success")) {
                        callback.onFailure(json, json.getAsJsonObject("item").getAsJsonObject("result").getAsJsonPrimitive("message").getAsString());
                    } else {
                        callback.onSuccess(json);
                    }
                } else if (type == 1) {
                    callback.onUpdate(json);
                } else if (type == 7) {
                    callback.onFailure(json, json.getAsJsonPrimitive("error").getAsString());
                    webSocket.close(1000, String.valueOf(TerminalChar));
                }
            } else if (json.has("error")) {
                callback.onFailure(json, json.getAsJsonPrimitive("error").getAsString());
                webSocket.close(1000, String.valueOf(TerminalChar));
            }
        }
        super.onMessage(webSocket, text);
    }

    /**
     * On open.
     *
     * @param webSocket the web socket
     * @param response  the response
     */
    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        sendData(webSocket, "{\"protocol\":\"json\",\"version\":1}");
    }

    /**
     * Send data.
     *
     * @param ws   the ws
     * @param data the data
     */
    private void sendData(@NotNull WebSocket ws, @NotNull String data) {
        logger.Debug(String.format("[%s] [%s] client send message [%s]", conversationSignature, question, data));
        ws.send(data + TerminalChar);
    }
}
