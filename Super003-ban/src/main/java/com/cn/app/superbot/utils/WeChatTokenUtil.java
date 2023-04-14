
package com.cn.app.superbot.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicLong;


/**
 * The enum Token manager.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
public enum WeChatTokenUtil {
    INSTANCE;

    private final Token token = new Token();
    private final AtomicLong tokenExpireTime = new AtomicLong(0L);

    /**
     * Gets wechat token.
     *
     * @param appid  the appid
     * @param secret the secret
     * @return the wechat token
     */
    public String getWechatToken(final String appid, final String secret) {
        if (System.currentTimeMillis() / 1000 >= tokenExpireTime.get()) {
            resetToken(appid, secret);
        }
        return token.getToken();
    }

    /**
     * Reset token.
     *
     * @param appid  the appid
     * @param secret the secret
     */
    private void resetToken(final String appid, final String secret) {
        final JSONObject block = JSONObject.parseObject(getAccessToken(appid, secret));
        long timestamp = System.currentTimeMillis() / 1000;
        String accessToken = block.getString("access_token");
        int expiresIn = block.getInteger("expires_in");
        token.setToken(accessToken);
        tokenExpireTime.set(timestamp + expiresIn);
    }

    /**
     * Gets access token.
     *
     * @param appid  the appid
     * @param secret the secret
     * @return the access token
     */
    private String getAccessToken(final String appid, final String secret) {
        final var accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        return WebClient.create()
                .get().uri(accessTokenUrl)
                .retrieve()
                .bodyToMono(String.class).block();
    }

    @Accessors(chain = true)
    @Data
    public static class Token {
        public String token;
    }
}
