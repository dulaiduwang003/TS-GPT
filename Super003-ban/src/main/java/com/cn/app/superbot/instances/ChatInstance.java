package com.cn.app.superbot.instances;

import com.cn.app.superbot.exception.ConversationException;
import com.cn.app.superbot.exception.ConversationExpiredException;
import com.cn.app.superbot.exception.ConversationLimitedException;
import com.cn.app.superbot.exception.ConversationUninitializedException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.cn.app.superbot.interfaces.Callback;
import com.cn.app.superbot.interfaces.Logger;
import com.cn.app.superbot.utils.ConversationWebsocket;
import lombok.SneakyThrows;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Chat instance.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@SuppressWarnings("all")
public class ChatInstance {
    /**
     * The constant threadPool.
     */
    public static ExecutorService threadPool = Executors.newFixedThreadPool(1);
    /**
     * The Client.
     */
    private final OkHttpClient client;
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
     * The Logger.
     */
    private final Logger logger;
    /**
     * The Locale.
     */
    private final String locale;
    /**
     * The Chat count.
     */
    private short chatCount;
    /**
     * The Max num conversation.
     */
    private int maxNumConversation = 15;
    /**
     * The Time.
     */
    private final long time;


    /**
     * Instantiates a new Chat instance.
     *
     * @param httpClientBuilder the http client builder
     * @param logger            the logger
     * @param locale            the locale
     * @throws IOException           the io exception
     * @throws ConversationException the conversation exception
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public ChatInstance(OkHttpClient.Builder httpClientBuilder, Logger logger, String locale) throws IOException, ConversationException {
        client = httpClientBuilder.dispatcher(new Dispatcher(threadPool)).build();
        this.logger = logger;
        this.locale = locale;
        logger.Debug("Creating Conversation ID");
        Request req = new Request.Builder().url("https://www.bing.com/turing/conversation/create").get().build();
        String s = Objects.requireNonNull(client.newCall(req).execute().body()).string();
        JsonObject json = JsonParser.parseString(Objects.requireNonNull(s)).getAsJsonObject();
        if (!json.getAsJsonObject("result").getAsJsonPrimitive("value").getAsString().equals("Success"))
            throw new ConversationException(json.getAsJsonObject("result").getAsJsonPrimitive("message").getAsString());
        time = new Date().getTime();
        chatCount = 0;
        conversationId = json.getAsJsonPrimitive("conversationId").getAsString();
        logger.Debug(String.format("New Conversation ID [%s]", conversationId));
        clientId = json.getAsJsonPrimitive("clientId").getAsString();
        conversationSignature = json.getAsJsonPrimitive("conversationSignature").getAsString();
    }


    /**
     * New question chat instance.
     *
     * @param question the question
     * @param callback the callback
     * @return the chat instance
     */
    @SneakyThrows
    synchronized public ChatInstance newQuestion(String question, Callback callback) {
        if (chatCount < 0) {
            logger.Error("Conversation is uninitialized");
            throw new ConversationUninitializedException();
        } else if (chatCount > maxNumConversation) {
            logger.Error("Conversation is limited");
            throw new ConversationLimitedException();
        } else if (new Date().getTime() - time > 360 * 1000) {
            logger.Error("Conversation has expired");
            throw new ConversationExpiredException();
        }
        Callback cb = new Callback() {
            @Override
            public void onSuccess(JsonObject rawData) {
                if (rawData.has("item") && rawData.getAsJsonObject("item").has("throttling") && rawData.getAsJsonObject("item").getAsJsonObject("throttling").has("maxNumUserMessagesInConversation")) {
                    maxNumConversation = rawData.getAsJsonObject("item")
                            .getAsJsonObject("throttling")
                            .getAsJsonPrimitive("maxNumUserMessagesInConversation").getAsInt();
                }
                callback.onSuccess(rawData);
            }

            @Override
            public void onFailure(JsonObject rawData, String cause) {
                callback.onFailure(rawData, cause);
            }

            @Override
            public void onUpdate(JsonObject rawData) {
                callback.onUpdate(rawData);
            }
        };

        Request request = new Request.Builder().get().url("wss://sydney.bing.com/sydney/ChatHub").build();
        logger.Debug(String.format("Add a question [%s] to the queue,the current length of the queue is %d", question, client.dispatcher().runningCallsCount()));
        client.newWebSocket(
                request,
                new ConversationWebsocket(
                        conversationId,
                        clientId,
                        conversationSignature,
                        question,
                        chatCount,
                        cb,
                        logger,
                        locale
                )
        );
        chatCount++;
        return this;
    }
}
