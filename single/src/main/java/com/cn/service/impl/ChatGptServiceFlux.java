package com.cn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cn.common.ChatGptCommon;
import com.cn.common.structure.ChatGptStructure;
import com.cn.dto.DialogueParameterDto;
import com.cn.entity.TsException;
import com.cn.enums.LevelEnum;
import com.cn.enums.ServerEnum;
import com.cn.mapper.TsExceptionMapper;
import com.cn.model.ChatGptDialogueModel;
import com.cn.model.ChatGptImageRecognitionModel;
import com.cn.service.OperationAbstract;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;

/**
 * GPT流对话 业务处理 抽象
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ChatGptServiceFlux extends OperationAbstract<DialogueParameterDto> {

    private final TsExceptionMapper tsExceptionMapper;

    @Override
    protected FluxOutcome streamConversations(final DialogueParameterDto obj) {
        ChatGptCommon.ModelObj modelObj;
        try {
            //获取额外参数
            final DialogueParameterDto.Extra extra = parseObject(obj.getExtra(), DialogueParameterDto.Extra.class);
            //获取数据集
            final List<ChatGptDialogueModel.Messages> messages = parseArray(obj.getMessages(), ChatGptDialogueModel.Messages.class);
            //获取当前指向模型
            modelObj = ChatGptCommon.pollGetKey(extra.getModelIndex(), false);
            final String key = modelObj.getKey();
            final Flux<String> stringFlux = WebClient.builder().baseUrl(ChatGptCommon.STRUCTURE.getRequestUrl())
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + key)
                    .codecs(item -> item.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
                    .build()
                    .post()
                    .uri("/chat/completions")
                    .body(BodyInserters.fromValue(
                            //封装MODEL
                            new ChatGptDialogueModel()
                                    .setMessages(messages)
                                    .setMax_tokens(modelObj.getMax_tokens())
                                    .setTemperature(modelObj.getTemperature())
                                    .setTop_p(modelObj.getTop_p())
                                    .setModel(modelObj.getModelName())))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (clientResponse -> clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                final String code = analysisErrorBody(errorBody);
                                connectionException(code, key);
                                return Mono.error(new RuntimeException());
                            })))
                    .bodyToFlux(String.class);

            return new FluxOutcome()
                    .setFlux(stringFlux).setVerify(extra.getIsFiltration());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 记录连接异常
     *
     * @param e   the e
     * @param key the key
     */
    private void connectionException(String e, final String key) {

        final TsException ts = new TsException();
        //记录对话异常
        String str = "当前GPT密钥:" + key + "貌似有问题,在对话时出现了:" + e + " 错误,请根据OPENAI提供的错误进行相关操作";
        ts.setLevel(LevelEnum.MIDDLE.getDesc())
                .setServerName(ServerEnum.CHAT.getDesc())
                .setCause(str);
        tsExceptionMapper.insert(ts);


    }


    @Override
    protected FluxOutcome streamImageConversations(final DialogueParameterDto obj) {

        final List<ChatGptImageRecognitionModel.Messages> messages = parseArray(obj.getMessages(), ChatGptImageRecognitionModel.Messages.class);
        //获取额外参数
        final DialogueParameterDto.Extra extra = parseObject(obj.getExtra(), DialogueParameterDto.Extra.class);
        //获取识别图像配置
        ChatGptStructure.ImageRecognitionConfig config = ChatGptCommon.STRUCTURE.getImageRecognitionConfig();
        //随机获取密钥
        final String key = ChatGptCommon.pollGetKey(null, true).getKey();
        try {
            //某个角落 就你和我
            final Flux<String> stringFlux = WebClient.builder().baseUrl(ChatGptCommon.STRUCTURE.getRequestUrl())
                    //从前从前
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + key)
                    .codecs(item -> item.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
                    .build()
                    .post()
                    //像突然土壤抓紧花的迷惑
                    .uri("/chat/completions")
                    .body(BodyInserters.fromValue(
                            new ChatGptImageRecognitionModel()
                                    .setModel(config.getModel())
                                    .setMax_tokens(config.getMax_tokens())
                                    .setMessages(messages)
                    ))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (clientResponse -> clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                final String code = analysisErrorBody(errorBody);
                                connectionException(code, key);
                                return Mono.error(new RuntimeException());
                            })))
                    //但偏偏 雨渐渐 大到我看你不见
                    .bodyToFlux(String.class);

            return new FluxOutcome()
                    .setFlux(stringFlux).setVerify(extra.getIsFiltration());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 解析openai状态码
     *
     * @param errorBody the error body
     * @return the string
     */
    private String analysisErrorBody(String errorBody) {
        final String errorCode = parseObject(errorBody).getString("error");
        final JSONObject jsonObject = parseObject(errorCode);
        return jsonObject.toString();

    }


    /**
     * 返回结构体
     */
    @Data
    @Accessors(chain = true)
    public static class FluxOutcome {
        //数据载体
        private Flux<String> flux;
        //是否进行文本安全过滤
        private boolean isVerify;

    }
}
