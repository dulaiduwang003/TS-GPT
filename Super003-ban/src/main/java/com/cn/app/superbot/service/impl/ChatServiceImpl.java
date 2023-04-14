package com.cn.app.superbot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cn.app.superbot.config.ServerConfig;
import com.cn.app.superbot.constants.GptConstants;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.ServerConstants;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.model.FaceModel;
import com.cn.app.superbot.model.GptFourModel;
import com.cn.app.superbot.model.GptTurboModel;
import com.cn.app.superbot.service.ChatService;
import com.cn.app.superbot.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;


/**
 * The type Gpt service.
 *
 * @author bdth
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ChatServiceImpl implements ChatService {

    /**
     * The Redis utils.
     */
    private final RedisUtils redisUtils;


    /**
     * The constant QUEUE_SIZE.
     */
    private static final int QUEUE_SIZE = 1;

    /**
     * The constant openUrl.
     */
    private static final String openUrl = "https://api.openai.com/v1/";

    /**
     * The Queue.
     */
    private final BlockingQueue<FaceModel> queue = new LinkedBlockingQueue<>(QUEUE_SIZE);


    /**
     * Chat gpt 4 json object.
     *
     * @param model the model
     * @return the json object
     */
    @Override
    public JSONObject chatGpt4(final GptFourModel model) {
        final ServerConfig config = (ServerConfig) redisUtils.getValue(ServerConstants.SERVER_CONFIG);
        //check whether the server is configured correctly
        if (config == null || config.chatGpt4 == null) {
            // throw warning exception
            throw new CustomException(MsgConstants.GPT_4_CONFIG_ERR, 500);
        }
        //This must be created each time, otherwise the WebClient in the cache will always be used
        final WebClient.Builder webClient = WebClient.builder();
        try {
            //routing
            switch (config.choose) {
                case 1, 3 -> webClient
                        .baseUrl(config.chatGpt4.baseUrl)
                        .defaultHeader("Authorization", "Bearer " + config.chatGpt4.getKey());
                case 2 -> {
                    //create proxy
                    final HttpClient httpClient = HttpClient.create()
                            .proxy(proxy -> proxy
                                    .type(ProxyProvider.Proxy.HTTP)
                                    .address(new InetSocketAddress(config.proxy.ip, config.proxy.port)));
                    webClient
                            .baseUrl(config.chatGpt4.baseUrl)
                            .clientConnector(new ReactorClientHttpConnector(httpClient));
                }
                default -> throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new CustomException(MsgConstants.TACTICS_ERR, 500);
        }
        return JSONObject.parseObject(webClient
                .build()
                .post()
                .body(BodyInserters.fromValue(model))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(60))
                .onErrorMap(TimeoutException.class, e -> new CustomException(MsgConstants.GPT_API_TIMEOUT, 400))
                .onErrorMap(Exception.class, e -> new CustomException(MsgConstants.GPT_API_ERR, 400))
                .block());
    }

    /**
     * Face string.
     *
     * @param model the model
     * @return the string
     */
    public JSONObject face(final FaceModel model) {
        final ServerConfig config = (ServerConfig) redisUtils.getValue(ServerConstants.SERVER_CONFIG);
        //determine whether parameters are configured
        if (config == null || config.sdUrl == null) {
            throw new CustomException(MsgConstants.SD_CONFIG_ERR, 500);
        }
        //determine whether  queue is full
        if (!queue.offer(model)) {
            throw new CustomException(MsgConstants.QUEUE_FILL_ERR, 400);
        }
        try {
            //queue
            FaceModel next = queue.take();
            final JSONObject content = build(new GptTurboModel()
                    .setMessages(
                            List.of(
                                    new GptTurboModel.Messages()
                                            .setRole("user")
                                            .setContent(GptConstants.TRANSLATION_ENGLISH + model.getPrompt())
                            )
                    ), "chat/completions");
            //use chat gpt to escape chinese
            next.setPrompt(content.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content"));
            //establish  connection
            final WebClient.Builder webClient = WebClient.builder();
            final JSONObject jsonObject = JSONObject.parseObject(webClient.build()
                    .post()
                    .uri(config.sdUrl)
                    .body(BodyInserters.fromValue(next))
                    .retrieve()
                    .bodyToMono(String.class)
                    //define  60 second timeout
                    .timeout(Duration.ofSeconds(60))
                    .onErrorMap(TimeoutException.class, e -> new CustomException(MsgConstants.GPT_API_TIMEOUT, 400))
                    .block());
            assert jsonObject != null;
            return jsonObject;
        } catch (Exception e) {
            throw new CustomException(MsgConstants.SD_API_ENABLE_ERR, 500);
        }
    }


    /**
     * Http string.
     *
     * @param body the body
     * @return the string
     */
    public JSONObject build(final Object body, final String url) {
        final ServerConfig serverConfig = (ServerConfig) redisUtils.getValue(ServerConstants.SERVER_CONFIG);
        if (serverConfig == null) {
            throw new CustomException(MsgConstants.SERVER_CONFIG_ERR, 500);
        }
        final WebClient.Builder webClient = WebClient.builder();
        switch (serverConfig.choose) {
            case 1 -> webClient
                    .baseUrl(openUrl)
                    .defaultHeader("Authorization", "Bearer " + serverConfig.key);
            case 2 -> {
                final HttpClient httpClient = HttpClient.create()
                        .proxy(proxy -> proxy
                                .type(ProxyProvider.Proxy.HTTP)
                                .address(new InetSocketAddress(serverConfig.proxy.ip, serverConfig.proxy.port)));
                webClient
                        .baseUrl(openUrl)
                        .clientConnector(new ReactorClientHttpConnector(httpClient));
            }
            case 3 -> webClient
                    .baseUrl(serverConfig.thirdParty.baseUrl)
                    .defaultHeader("Authorization", "Bearer " + serverConfig.thirdParty.token);
            default -> throw new CustomException(MsgConstants.TACTICS_ERR, 500);
        }
        try {
            return JSONObject.parseObject(
                    webClient.build()
                            .post()
                            .uri(url)
                            .body(BodyInserters.fromValue(body))
                            .retrieve()
                            .bodyToMono(String.class)
                            .timeout(Duration.ofSeconds(60))
                            .onErrorMap(TimeoutException.class, e -> new CustomException(MsgConstants.GPT_API_TIMEOUT, 400))
                            .block()
            );
        } catch (Exception e) {
            throw new CustomException(MsgConstants.GPT_API_ERR, 500);
        }
    }


}
