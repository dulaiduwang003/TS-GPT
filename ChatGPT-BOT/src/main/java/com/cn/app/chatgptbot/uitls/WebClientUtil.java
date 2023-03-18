
package com.cn.app.chatgptbot.uitls;

import com.alibaba.fastjson.JSONObject;
import com.cn.app.chatgptbot.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

/**
 * The type Web client util.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Slf4j
@RequiredArgsConstructor
public final class WebClientUtil {



    /**
     * Intranet json object.
     *
     * @param body the body
     * @return the json object
     */
    public static String intranet(final Object body) {
        JSONObject json = new JSONObject();
        json.put("param", body);
        return WebClient.builder()
                .baseUrl(GptUtil.bingUrl)
                .build()
                .post()
                .body(BodyInserters.fromValue(json))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(120))
                .onErrorMap(Exception.class, e -> new CustomException("使用该功能的用户过多 请十分钟后再试"))
                .block();
    }


    /**
     * Build string.
     *
     * @param connector the connector
     * @param url       the url
     * @param body      the body
     * @param openKey   the open key
     * @return the string
     */
    public static JSONObject build(ClientHttpConnector connector, final String url, final Object body, final String openKey) {
        final String block = WebClient.builder()
                .clientConnector(connector)
                .baseUrl("https://api.openai.com/v1/")
                .defaultHeader("Authorization", "Bearer " + openKey)
                .build()
                .post()
                .uri(url)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            JSONObject errorObj = JSONObject.parseObject(errorBody);
                            final String errorCode = errorObj.getString("error");
                            final JSONObject jsonObject = JSONObject.parseObject(errorCode);
                            final String type = jsonObject.getString("type");
                            final String code = jsonObject.getString("code");
                            if ("access_terminated".equals(type)) {
                                GptUtil.removeKey(Collections.singletonList(openKey));
                                GptUtil.getRandomKey(openKey);
                                return Mono.error(new CustomException("目前分配的OpenKey已遭封禁(系统已重新分配KEY),请尝试重新发送消息"));
                            } else if ("invalid_request_error".equals(type)) {
                                if ("invalid_api_key".equals(code)) {
                                    GptUtil.removeKey(Collections.singletonList(openKey));
                                    GptUtil.getRandomKey(openKey);
                                    return Mono.error(new CustomException("目前分配的OpenKey已经失效(系统已重新分配KEY),请尝试请重新发送消息"));
                                } else {
                                    GptUtil.removeKey(Collections.singletonList(openKey));
                                    GptUtil.getRandomKey(openKey);
                                    return Mono.error(new CustomException("请求已被OpenAi拒绝受理(系统已重新分配KEY),请尝试重新发送消息"));
                                }
                            } else {
                                return Mono.error(new CustomException("请求过于频繁,请稍后再试"));
                            }

                        }))
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(120))
                .onErrorMap(TimeoutException.class, e -> new CustomException("回复时间过长,建议点击垃圾箱清理会话数据"))
                .onErrorMap(Exception.class, e -> new CustomException("请求过于频繁,请稍后再试"))
                .block();

        return JSONObject.parseObject(block);
    }


}
