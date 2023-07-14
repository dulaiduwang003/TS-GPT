package com.cn.app.superbot.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.app.superbot.config.ServerConfig;
import com.cn.app.superbot.constants.GptConstants;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.ServerConstants;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.handle.Chat;
import com.cn.app.superbot.interfaces.Callback;
import com.cn.app.superbot.model.FaceModel;
import com.cn.app.superbot.model.GptFourModel;
import com.cn.app.superbot.model.GptTurboModel;
import com.cn.app.superbot.service.ChatService;
import com.cn.app.superbot.utils.NumericalUtils;
import com.cn.app.superbot.utils.RedisUtils;
import com.cn.app.superbot.utils.UploadUtil;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;


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
     * The Upload util.
     */
    private final UploadUtil uploadUtil;


    /**
     * Image recognition json object.
     *
     * @param file the file
     * @return the json object
     */
    @Override
    public JSONObject imageRecognition(final MultipartFile file) {
        try {
            final String upload = uploadUtil.upload(file);
            //fetch
            final JSONObject json = this.chatGpt4(new GptFourModel().setMessages(List.of(
                    new GptFourModel.Messages().setRole("user").setContent(GptConstants.IMAGE_CONTENT + upload)
            )));
            json.put("image", upload);
            return json;
        } catch (Exception e) {
            throw new CustomException(MsgConstants.UPLOAD_ERR_IMAGE, 500);
        }
    }

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
                            .defaultHeader("Authorization", "Bearer " + config.chatGpt4.getKey())
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
        if (NumericalUtils.isNullOrEmpty(serverConfig)) {
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
                        .defaultHeader("Authorization", "Bearer " + serverConfig.key)
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
            e.printStackTrace();
            throw new CustomException(MsgConstants.GPT_API_ERR, 500);
        }
    }

    /**
     * Bing json object.
     *
     * @param parameter the parameter
     * @return the json object
     */
    @Override
    public String bing(final String parameter) {
        final String[] result = new String[1];
        //get  server configuration
        final ServerConfig config = (ServerConfig) redisUtils.getValue(ServerConstants.SERVER_CONFIG);
        CountDownLatch downLatch = new CountDownLatch(1);
        // presence of the configuration exists
        if (config == null || config.proxy == null || config.cookie == null) {
            throw new CustomException(MsgConstants.NEW_BING_CONFIG_ERR, 500);
        }
        try {

            Chat chat;
            try {
                switch (config.choose) {
                    //turn on  proxy
                    case 2, 3 -> chat = new Chat("_U=" + config.getCookie(),
                            false
                    ).setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxy().ip, config.getProxy().port)));

                    case 1 -> chat = new Chat("_U=" + config.getCookie(),
                            false
                    );
                    default -> throw new RuntimeException();
                }
            } catch (Exception e) {
                throw new CustomException(MsgConstants.TACTICS_ERR, 500);
            }
            chat.newChat().newQuestion(parameter, new Callback() {
                /**
                 * On success.
                 *
                 * @param rawData the raw data
                 */
                @Override
                public void onSuccess(JsonObject rawData) {
                    downLatch.countDown();
                }

                /**
                 * On failure.
                 *
                 * @param rawData the raw data
                 * @param cause   the cause
                 */
                @Override
                public void onFailure(JsonObject rawData, String cause) {
                    downLatch.countDown();
                    throw new CustomException(MsgConstants.NEW_BING_SERVER_ERR, 500);
                }

                /**
                 * On update.
                 *
                 * @param rawData the raw data
                 */
                @Override
                public void onUpdate(JsonObject rawData) {
                    final JSONObject jsonObject = JSONObject.parseObject(rawData.toString());
                    //read  data for
                    for (Object arguments : jsonObject.getJSONArray("arguments")) {
                        final JSONObject json = JSONObject.parseObject(arguments.toString());
                        // whether it's  message
                        if (json.containsKey("messages") && (!json.containsKey("cursor"))) {
                            final JSONArray jsonArray = json.getJSONArray("messages").getJSONObject(0).getJSONArray("adaptiveCards");
                            final JSONObject body = jsonArray.getJSONObject(0).getJSONArray("body").getJSONObject(0);
                            //there data
                            if (body.containsKey("text")) {
                                // populate  data
                                result[0] = (body.getString("text"));
                            }
                        }
                    }
                }
            });
            // response for more than 60 seconds exception 's thrown
            if (!downLatch.await(60, TimeUnit.SECONDS)) {
                throw new TimeoutException();
            }
        } catch (Exception e) {
            throw new CustomException(MsgConstants.NEW_BING_TIME_ERR, 500);
        }
        return result[0];
    }

}
