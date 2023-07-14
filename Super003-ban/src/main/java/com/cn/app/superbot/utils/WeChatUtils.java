package com.cn.app.superbot.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.UserConstants;
import com.cn.app.superbot.dto.WeChatLoginDto;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.model.MsgSecCheckModel;
import com.cn.app.superbot.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@SuppressWarnings("all")
@RequiredArgsConstructor
@Slf4j
public final class WeChatUtils {
    /**
     * The App id.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    @Value("${we-chat.appId}")
    private String appId;


    /**
     * The Secret.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    @Value("${we-chat.secret}")
    private String secret;


    private final RedisUtils redisUtils;

    /**
     * Gets WeChat open id.
     *
     * @param dto the dto
     * @return the open id
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public String getOpenId(final WeChatLoginDto dto) {
        final String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + this.appId +
                "&secret=" + this.secret + "&js_code=" + dto.getCode() + "&grant_type=authorization_code";
        final String response = WebClient.create().get().uri(url).retrieve().bodyToMono(String.class).block();
        final JSONObject block = JSONObject.parseObject(response);
        try {
            final String openid = block.getString("openid");
            assert openid != null;
            return openid;
        } catch (Exception e) {
            throw new CustomException(MsgConstants.WECHAT_API_ERR, 500);
        }
    }

    /**
     * Filtration.
     *
     * @param content the content
     * @param openId  the open id
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public void filtration(final String content) {
        final UserModel userModel = (UserModel) redisUtils.hashGet(UserConstants.USER_INFO, StpUtil.getLoginId().toString());
        JSONObject jsonObject = null;
        try {
            final String response = WebClient.create().post().uri("https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + WeChatTokenUtil.INSTANCE.getWechatToken(appId, secret))
                    .body(BodyInserters.fromValue(new MsgSecCheckModel().setContent(content).setOpenid(userModel.getOpenId())))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            final JSONObject json = JSONObject.parseObject(response);
            assert json != null;
            jsonObject = JSONObject.parseObject(json.getString("result"));
        } catch (Exception e) {
            throw new CustomException(MsgConstants.WECHAT_API_ERR, 500);
        }
        if (!("pass".equals(jsonObject.getString("suggest")))) {
            throw new CustomException(MsgConstants.SUSCEPTIBLE_ERR, 400);
        }

    }

}
