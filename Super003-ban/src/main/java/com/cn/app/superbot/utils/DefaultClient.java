package com.cn.app.superbot.utils;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * The type Default client.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@SuppressWarnings("all")
public class DefaultClient {
    /**
     * The Builder.
     */
    private final OkHttpClient.Builder builder;
    /**
     * The Cookie.
     */
    private final String cookie;
    /**
     * The Bypass cn.
     */
    private final boolean bypassCN;

    /**
     * The Oversea i ps.
     */
    private final String[] overseaIPs = {
            "8.8.8.8"
    };

    /**
     * The Ip.
     */
    private final String IP;


    /**
     * Instantiates a new Default client.
     *
     * @param BypassCN the bypass cn
     * @param Cookie   the cookie
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public DefaultClient(Boolean BypassCN, String Cookie) {
        bypassCN = BypassCN;
        IP = getRandomIP();
        cookie = Cookie;
        builder = new OkHttpClient.Builder().addInterceptor(new headerInterceptor());
    }

    private String getRandomIP() {
        return overseaIPs[new Random().nextInt(overseaIPs.length)];
    }

    /**
     * Returns the OkHttpClient instance associated with this DefaultClient.
     *
     * @return the OkHttpClient instance associated with this DefaultClient.
     */
    public OkHttpClient getClient() {
        return builder.build();
    }

    /**
     * The type Header interceptor.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    private class headerInterceptor implements Interceptor {
        /**
         * Intercept response.
         *
         * @param chain the chain
         * @return the response
         * @throws IOException the io exception
         */
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request.Builder b = chain.request().newBuilder();
            b.addHeader("Cookie", cookie);
            b.addHeader("Origin", "https://www.bing.com");
            b.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.24");
            b.addHeader("x-ms-client-request-id", UUID.randomUUID().toString());
            b.addHeader("x-ms-useragent", "azsdk-js-api-client-factory/1.0.0-beta.1 core-rest-pipeline/1.10.0 OS/Win32");
            if (bypassCN) {
                b.addHeader("X-Forwarded-For", IP);
            }
            return chain.proceed(b.build());
        }
    }
}