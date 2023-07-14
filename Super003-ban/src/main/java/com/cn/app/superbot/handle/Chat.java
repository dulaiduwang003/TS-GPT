package com.cn.app.superbot.handle;


import com.cn.app.superbot.exception.ConversationException;
import com.cn.app.superbot.instances.ChatInstance;
import com.cn.app.superbot.interfaces.Logger;
import com.cn.app.superbot.utils.DefaultClient;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.Proxy;

/**
 * The type Chat.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public class Chat {
    /**
     * The C.
     */
    private final OkHttpClient.Builder c;
    /**
     * The Logger.
     */
    private final Logger logger;
    /**
     * The Locale.
     */
    private final String locale;


    /**
     * Instantiates a new Chat.
     *
     * @param defaultCookie the default cookie
     * @param bypassCN      the bypass cn
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Chat(String defaultCookie, Boolean bypassCN) {
        c = new DefaultClient(bypassCN, defaultCookie).getClient().newBuilder();
        this.locale = "zh-CN";
        this.logger = new Logger() {
            @Override
            public void Info(String log) {
            }

            @Override
            public void Error(String log) {
            }

            @Override
            public void Warn(String log) {
            }

            @Override
            public void Debug(String log) {
            }
        };
    }


    /**
     * Instantiates a new Chat.
     *
     * @param defaultCookie the default cookie
     * @param bypassCN      the bypass cn
     * @param logger        the logger
     */
    public Chat(String defaultCookie, Boolean bypassCN, Logger logger) {
        c = new DefaultClient(bypassCN, defaultCookie).getClient().newBuilder();
        this.locale = "zh-CN";
        this.logger = logger;
    }

    /**
     * Instantiates a new Chat.
     *
     * @param defaultCookie the default cookie
     * @param bypassCN      the bypass cn
     * @param locale        the locale
     */
    public Chat(String defaultCookie, Boolean bypassCN, String locale) {
        c = new DefaultClient(bypassCN, defaultCookie).getClient().newBuilder();
        this.locale = locale;
        this.logger = new Logger() {
            @Override
            public void Info(String log) {
            }

            @Override
            public void Error(String log) {
            }

            @Override
            public void Warn(String log) {
            }

            @Override
            public void Debug(String log) {
            }
        };
    }

    /**
     * Close.
     */
    public void close() {
        OkHttpClient client = c.build();
        client.connectionPool().evictAll();
        client.dispatcher().executorService().shutdown();
    }

    /**
     * Sets proxy.
     *
     * @param proxy the proxy
     * @return the proxy
     */
    public Chat setProxy(Proxy proxy) {
        c.proxy(proxy);
        return this;
    }


    public Chat(String defaultCookie, Boolean bypassCN, Logger logger, String locale) {
        c = new DefaultClient(bypassCN, defaultCookie).getClient().newBuilder();
        this.locale = locale;
        this.logger = logger;
    }

    public ChatInstance newChat() {
        try {
            return new ChatInstance(c, logger, locale);
        } catch (IOException | ConversationException e) {
            throw new RuntimeException(e);
        }
    }
}
