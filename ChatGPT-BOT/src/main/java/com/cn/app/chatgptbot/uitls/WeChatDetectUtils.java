
package com.cn.app.chatgptbot.uitls;

import com.alibaba.fastjson.JSONObject;
import com.cn.app.chatgptbot.exception.CustomException;
import com.cn.app.chatgptbot.model.MsgSecCheckModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The type We chat detect utils.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Component
public class WeChatDetectUtils {


    /**
     * App id.
     */
    @Value("${we-chat.appId}")
    private String appId;

    /**
     * Secret.
     */
    @Value("${we-chat.secret}")
    private String secret;


    private String parse(final String url) {
        final String response = WebClient.create().get().uri(url).retrieve().bodyToMono(String.class).block();

        final JSONObject block = JSONObject.parseObject(response);

        assert block != null;

        return block.getString("openid");
    }


    /**
     * Gets user open id.
     *
     * @param code the code
     * @return the user open id
     */
    public String getUserOpenId(final String code) {
        final String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId +
                "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        return parse(url);
    }

    /**
     * Filtration.
     *
     * @param content the content
     * @param openId  the open id
     */
    public void filtration(final String content, final String openId) {
        // get token
        final String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + secret;
        final String accessTokenResponse = WebClient.create()
                .get().uri(accessTokenUrl)
                .retrieve()
                .bodyToMono(String.class).block();

        final JSONObject block = JSONObject.parseObject(accessTokenResponse);
        // tencent api not err 😀
        assert block != null;

        final String accessToken = block.getString("access_token");
        // filtration
        final String response = WebClient.create().post().uri("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken)
                .body(BodyInserters.fromValue(new MsgSecCheckModel().setContent(content).setOpenid(openId)))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        final JSONObject json = JSONObject.parseObject(response);
        assert json != null;
        final JSONObject jsonObject = JSONObject.parseObject(json.getString("result"));
        //paa!
        if (!("pass".equals(jsonObject.getString("suggest")))) {
            throw new CustomException("请勿发布违反微信社区规定的发言");
        }
    }
}
