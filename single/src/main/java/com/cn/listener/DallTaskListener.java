package com.cn.listener;

import com.alibaba.fastjson.JSONObject;
import com.cn.common.DallCommon;
import com.cn.common.PoolCommon;
import com.cn.constant.DrawingConstant;
import com.cn.constant.DrawingStatusConstant;
import com.cn.entity.TsException;
import com.cn.entity.TsGenerateDrawing;
import com.cn.enums.DrawingTypeEnum;
import com.cn.enums.FileEnum;
import com.cn.enums.LevelEnum;
import com.cn.enums.ServerEnum;
import com.cn.mapper.TsExceptionMapper;
import com.cn.mapper.TsGenerateDrawingMapper;
import com.cn.model.DallModel;
import com.cn.structure.TaskStructure;
import com.cn.utils.StringUtils;
import com.cn.utils.UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.alibaba.fastjson.JSON.parseObject;


/**
 * DALL绘图监听器
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@SuppressWarnings("all")
@Configuration
public class DallTaskListener {


    private final RedisTemplate<String, Object> redisTemplate;

    private final WebClient.Builder webClient;

    private final ThreadPoolExecutor threadPoolExecutor;

    private final TsExceptionMapper tsExceptionMapper;

    private final UploadUtil uploadUtil;

    private final TsGenerateDrawingMapper tsGenerateDrawingMapper;
    private Semaphore semaphore;

    /**
     * 你会不会突然的出现
     */
    @EventListener(ApplicationReadyEvent.class)
    public void dallListening() {
        semaphore = new Semaphore(PoolCommon.STRUCTURE.getDallConcurrent());
        threadPoolExecutor.execute(() -> {
            while (true) {
                try {
                    semaphore.acquire();
                    //每三秒从队列中获取数据
                    final TaskStructure ts = (TaskStructure) redisTemplate.opsForList().rightPop(DrawingConstant.DALL_TASK_QUEUE, 2, TimeUnit.SECONDS);
                    try {
                        if (ts != null) {
                            this.handleDallGenerate(ts.getExtra(), ts.getTaskId());
                        }
                    } catch (Exception e) {
                        //设置报错信息
                        redisTemplate.opsForValue()
                                .set(DrawingConstant.TASK + ts.getTaskId(), ts
                                        .setStatus(DrawingStatusConstant.DISUSE), 600, TimeUnit.SECONDS);
                        //设置绘图失败标识
                        tsGenerateDrawingMapper.updateById(new TsGenerateDrawing()
                                .setStatus(DrawingStatusConstant.DISUSE)
                                .setGenerateDrawingId(ts.getTaskId()));
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    semaphore.release(); // 释放信号量许可
                }


            }
        });
    }


    /**
     *  处理DAll生成任务
     *
     * @param o      the o
     * @param taskId the task id
     */
    private void handleDallGenerate(final Object o, final String taskId) {
        final DallModel model = (DallModel) o;
        //dall有类型 文生图 图生图 局部绘图
        //根据传参区分 具体操作
        final Map<String, Object> map = new HashMap<>();
//        final MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.put("prompt", model.getPrompt());
        if (StringUtils.notEmpty(model.getMask())) {
            map.put("mask", model.getMask());
        }
        if (StringUtils.notEmpty(model.getImage())) {
            map.put("image", model.getImage());
        }
        map.put("model", model.getModel());
        map.put("n", model.getN());
        map.put("size", model.getSize());
        String uri = "/images/generations";
//        if (StringUtils.notEmpty(model.getImage()) && StringUtils.notEmpty(model.getMask())) {
//            //局部绘图
//            uri = "/images/edits";
//        } else if (StringUtils.notEmpty(model.getImage()) && !StringUtils.notEmpty(model.getMask())) {
//            // 图生图
//            map.remove("prompt");
//            uri = "/images/variations";
//        }
        //设置执行任务
        final TaskStructure taskStructure = new TaskStructure().setDrawingType(DrawingTypeEnum.DALL.getDec()).setTaskId(taskId).setPrompt(model.getPrompt());

        final TsGenerateDrawing tsGenerateDrawing = new TsGenerateDrawing().setGenerateDrawingId(taskId);
        final String key = DallCommon.pollGetKey();
        try {
            //设置当前任务
            redisTemplate.opsForHash().put(DrawingConstant.DALL_EXECUTION, taskId, taskId);

            final String block = webClient.baseUrl(DallCommon.STRUCTURE.getRequestUrl())
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + key)
                    .codecs(item -> item.defaultCodecs().maxInMemorySize(20 * 1024 * 1024)).build()
                    .post()
                    .uri(uri)
                    .body(BodyInserters.fromValue(map))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (clientResponse -> clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                final String errorCode = parseObject(errorBody).getString("error");
                                final JSONObject jsonObject = parseObject(errorCode);
                                connectionException(jsonObject.toString(), key);
                                return Mono.error(new RuntimeException());
                            })))
                    .bodyToMono(String.class).block();
            final String string = JSONObject.parseObject(block).getJSONArray("data").getJSONObject(0).getString("url");
            //上传至阿里云
            final String drawingUrl = uploadUtil.uploadImageFromUrl(string, FileEnum.DRAWING.getDec());
            tsGenerateDrawing
                    .setStatus(DrawingStatusConstant.SUCCEED)
                    .setUrl(drawingUrl);
            //设置构建成功
            redisTemplate.opsForValue().set(DrawingConstant.TASK + taskId,
                    taskStructure
                            .setImageUrl(drawingUrl)
                            .setStatus(DrawingStatusConstant.SUCCEED)
                    , 600, TimeUnit.SECONDS);
        } catch (Exception e) {
            connectionException(e.getMessage(), key);
            tsGenerateDrawing.setStatus(DrawingStatusConstant.DISUSE);
            //设置返回错误结果
            redisTemplate.opsForValue()
                    .set(DrawingConstant.TASK + taskId, taskStructure
                            .setStatus(DrawingStatusConstant.DISUSE), 600, TimeUnit.SECONDS);
        } finally {
            redisTemplate.opsForHash().delete(DrawingConstant.DALL_EXECUTION, taskId);
            tsGenerateDrawingMapper.updateById(tsGenerateDrawing);
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

}
