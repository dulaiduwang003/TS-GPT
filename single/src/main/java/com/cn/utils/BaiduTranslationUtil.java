package com.cn.utils;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.Random;

/**
 * 雨纷纷旧故里草木深
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class BaiduTranslationUtil {

    /**
     * The Appid.
     */
    @Value("${baidu-translation.appid}")
    private String appid;

    /**
     * The Secret.
     */
    @Value("${baidu-translation.secret}")
    private String secret;

    /**
     * The constant URL.
     */
    private static final String URL = "https://api.fanyi.baidu.com/api/trans/vip/translate";

    /**
     * The Web client builder.
     */
    private final WebClient.Builder webClientBuilder;

    /**
     * Translate string.
     *
     * @param parameter the parameter
     * @param to        the to
     * @return the string
     * @throws Exception the exception
     */
    public String translate(String parameter, String to) throws Exception {
        Random random = new Random(10);
        String salt = Integer.toString(random.nextInt());
        String appid = this.appid + parameter + salt + this.secret;
        String sign = SaSecureUtil.md5(appid);
        // 封装请求参数
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("q", parameter);
        paramMap.add("appid", this.appid);
        paramMap.add("salt", salt);
        paramMap.add("sign", sign);
        paramMap.add("from", "auto");
        paramMap.add("to", to);
        final WebClient build = webClientBuilder
                .baseUrl(URL)
                .build();
        // 发送POST请求，获取结果
        final String block = build.post()
                .uri(UriBuilder::build)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(paramMap))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        final JSONObject jsonObject = JSONObject.parseObject(block);
        assert jsonObject != null;
        final JSONArray transResult = jsonObject.getJSONArray("trans_result");
        return transResult.getJSONObject(0).getString("dst");
    }

    /**
     * Chinese translation string.
     *
     * @param parameter the parameter
     * @return the string
     * @throws Exception the exception
     */
    public String chineseTranslation(final String parameter) throws Exception {
        return translate(parameter, "zh");
    }

    /**
     * English translation string.
     *
     * @param parameter the parameter
     * @return the string
     * @throws Exception the exception
     */
    public String englishTranslation(final String parameter) throws Exception {
        return translate(parameter, "en");
    }

}
