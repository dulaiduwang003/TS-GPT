
package com.cn.app.chatgptbot.uitls;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;


/**
 * The enum Token manager.
 * @author bdth
 * @email 2074055628@qq.com
 */
public enum TokenManager {
    INSTANCE;

    /**
     * The Token.
     */
    private final Token token = new Token();


    /**
     * Gets wechat token.
     *
     * @return the weChaToken
     */
    public String getWechatToken(final String appid, final String secret) {
        var optionalToken = Optional.of(token);
        return optionalToken.filter(token -> !isTokenExpired()).map(Token::getToken).orElseGet(() -> {
            resetToken(appid, secret);
            return token.getToken();
        });
    }

    /**
     * Is token expired boolean.
     *
     * @return the boolean
     */
    private boolean isTokenExpired() {
        var currentTime = System.currentTimeMillis() / 1000;
        var tokenTimestamp = token.getTimestamp();
        return currentTime > tokenTimestamp;
    }

    /**
     * Gets access token.
     *
     * @return the access token
     */
    private String getAccessToken(final String appid, final String secret) {
        final var accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        return WebClient.create()
                .get().uri(accessTokenUrl)
                .retrieve()
                .bodyToMono(String.class).block();
    }

    /**
     * Reset token.
     */
    private void resetToken(final String appid, final String secret) {
        final JSONObject block = JSONObject.parseObject(getAccessToken(appid, secret));
        var timestamp = System.currentTimeMillis() / 1000;
        token.setToken(block.getString("access_token")).setTimestamp(block.getInteger("expires_in") + timestamp);
    }

    /**
     * The type Token.
     */

    @Accessors(chain = true)
    @Data
    public static class Token {
        public String token;
        public long timestamp;
    }
}