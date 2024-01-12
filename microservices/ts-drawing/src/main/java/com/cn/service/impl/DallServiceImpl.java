package com.cn.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cn.common.DallCommon;
import com.cn.constant.DrawingConstant;
import com.cn.constant.DrawingStatusConstant;
import com.cn.dto.DallTaskDto;
import com.cn.dto.DialogueImageDto;
import com.cn.entity.TsDialogueDrawing;
import com.cn.entity.TsException;
import com.cn.entity.TsGenerateDrawing;
import com.cn.enums.DrawingTypeEnum;
import com.cn.enums.FileEnum;
import com.cn.enums.LevelEnum;
import com.cn.enums.ServerEnum;
import com.cn.mapper.TsDialogueDrawingMapper;
import com.cn.mapper.TsExceptionMapper;
import com.cn.mapper.TsGenerateDrawingMapper;
import com.cn.model.DallModel;
import com.cn.model.DialogueGenerateModel;
import com.cn.service.DallService;
import com.cn.structure.TaskStructure;
import com.cn.utils.*;
import com.cn.vo.DialogueImageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * The type Dall service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DallServiceImpl implements DallService {

    private final BaiduTranslationUtil baiduTranslationUtil;

    private final TsDialogueDrawingMapper tsDialogueDrawingMapper;

    private final TsGenerateDrawingMapper tsGenerateDrawingMapper;

    private final TsExceptionMapper tsExceptionMapper;

    private final UploadUtil uploadUtil;

    private final DrawingUtils drawingUtils;

    private final RedisUtils redisUtils;

    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * Dialog generation img dialogue image vo.
     *
     * @param dto the dto
     * @return the dialogue image vo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DialogueImageVo dialogGenerationImg(final DialogueImageDto dto) {
        //校验用户会员身份是否过期
        UserUtils.membershipHasExpiredText(StpUtil.getLoginIdAsLong());
        final String key = DallCommon.pollGetKey();
        try {
            final String block = WebClient.builder().baseUrl(DallCommon.STRUCTURE.getRequestUrl())
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + key)
                    .codecs(item -> item.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
                    .build()
                    .post()
                    .uri("/images/generations")
                    .body(BodyInserters.fromValue(new DialogueGenerateModel().setPrompt(dto.getPrompt())))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (clientResponse -> clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                final String errorCode = parseObject(errorBody).getString("error");
                                final JSONObject jsonObject = parseObject(errorCode);
                                connectionException(jsonObject.toString(), key);
                                return Mono.error(new RuntimeException());
                            })))
                    .bodyToMono(String.class).block();
            //解析JSON
            final JSONObject jsonObject = JSONObject.parseObject(block);
            assert jsonObject != null;
            final JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
            String revisedPrompt = data.getString("revised_prompt");
            //上传数据到阿里云OSS 不然回显过慢
            final String url = uploadUtil.uploadImageFromUrl(data.getString("url"), FileEnum.DRAWING.getDec());
            tsDialogueDrawingMapper.insert(new TsDialogueDrawing().setUrl(url));
            synchronized (this) {
                try {
                    //百度翻译API 单 1秒qs
                    revisedPrompt = baiduTranslationUtil.chineseTranslation(revisedPrompt);
                } catch (Exception e) {
                    tsExceptionMapper.insert(new TsException()
                            .setServerName(ServerEnum.DRAWING.getDesc())
                            .setLevel(LevelEnum.LOW.getDesc())
                            .setCause("调取百度翻译API失败,此报错信息不会影响生成结果,如觉得不需要 请忽略这条信息")
                    );
                }
            }
            return new DialogueImageVo().setRevisedPrompt(revisedPrompt).setUrl(url);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    /**
     * 记录连接异常
     */
    private void connectionException(String e, final String key) {
        final TsException dto = new TsException();
        //记录对话异常
        String str = "当前GPT密钥:" + key + "貌似有问题,在绘图时出现了:" + e + " 错误,请根据OPENAI提供的错误码进行相关操作";
        dto.setLevel(LevelEnum.MIDDLE.getDesc())
                .setServerName(ServerEnum.DRAWING.getDesc())
                .setCause(str);
        tsExceptionMapper.insert(new TsException());
    }

    /**
     * Add dall task string.
     *
     * @param dto the dto
     * @return the string
     */
    @Override
    public String addDallTask(final DallTaskDto dto) {
        //校验用户会员身份是否过期
        UserUtils.membershipHasExpiredText(StpUtil.getLoginIdAsLong());
        final Long currentLoginId = drawingUtils.verifyTask();
        //生成任务标识
        final String taskId = String.valueOf(UUID.randomUUID());
        final String actualTaskId = DrawingConstant.TASK + taskId;
        //设置个人任务队列
        final TaskStructure taskStructure = new TaskStructure()
                //设置任务状态为 构件中
                .setTaskId(taskId).setStatus(DrawingStatusConstant.PENDING).setDrawingType(DrawingTypeEnum.DALL.getDec()).setPrompt(dto.getPrompt());
        //设置个人任务
        redisUtils.setValueTimeout(actualTaskId, taskStructure, 1800);
        //填充模型
        final DallModel dallModel = new DallModel().setSize(dto.getSize()).setMask(dto.getMask()).setPrompt(dto.getPrompt()).setImage(dto.getImage());
        tsGenerateDrawingMapper.insert(new TsGenerateDrawing().setStatus(DrawingStatusConstant.PENDING).setPrompt(dto.getPrompt()).setGenerateDrawingId(taskId).setType(DrawingTypeEnum.DALL.getDec()).setUserId(currentLoginId));
        //加入任务队列
        redisTemplate.opsForList().leftPush(DrawingConstant.DALL_TASK_QUEUE, taskStructure.setExtra(dallModel));
        return taskId;
    }
}
