package com.cn.listener;

import com.alibaba.fastjson.JSONObject;
import com.cn.common.PoolCommon;
import com.cn.common.SdCommon;
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
import com.cn.model.SdModel;
import com.cn.structure.TaskStructure;
import com.cn.utils.UploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * SD任务监听器
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@SuppressWarnings("all")
@Configuration
public class SdTaskListener {

    private final RedisTemplate<String, Object> redisTemplate;

    private final WebClient.Builder webClient;

    private final TsExceptionMapper tsExceptionMapper;
    private Semaphore semaphore;

    private final ThreadPoolExecutor threadPoolExecutor;

    private final UploadUtil uploadUtil;

    private final TsGenerateDrawingMapper tsGenerateDrawingMapper;


    /**
     * 任务处理器
     */
    @EventListener(ApplicationReadyEvent.class)
    public void sdListening() {
        semaphore = new Semaphore(PoolCommon.STRUCTURE.getSdConcurrent());
        threadPoolExecutor.execute(() -> {
            while (true) {
                try {
                    semaphore.acquire();
                    //每三秒从队列中获取数据
                    final TaskStructure ts = (TaskStructure) redisTemplate.opsForList().rightPop(DrawingConstant.SD_TASK_QUEUE, 2, TimeUnit.SECONDS);
                    if (ts != null) {
                        this.handleSdGenerate(ts.getExtra(), ts.getTaskId());
                    }
                } catch (Exception e) {
                    log.error("SD绘图异常 原因:{} 位置:{}", e.getMessage(), e.getClass());
                } finally {
                    semaphore.release(); // 释放信号量许可
                }

            }
        });
    }


    /**
     * 处理SD生成任务
     *
     * @param o      the o
     * @param taskId the task id
     */
    private void handleSdGenerate(final Object o, final String taskId) {
        final SdModel model = (SdModel) o;

        //提取出上传图片
        final List<String> initImages = model.getInit_images();
        //路由
        String router;
        if (initImages != null && !initImages.isEmpty()) {
            router = "/img2img";
        } else {
            router = "/txt2img";
        }
        //设置执行任务
        final TaskStructure taskStructure = new TaskStructure().setDrawingType(DrawingTypeEnum.SD.getDec()).setTaskId(taskId).setPrompt(model.getPrompt());

        final TsGenerateDrawing tsGenerateDrawing = new TsGenerateDrawing().setGenerateDrawingId(taskId);
        try {
            redisTemplate.opsForValue().set(DrawingConstant.SD_EXECUTION, taskId);

            //状态设置为 构建中
            final String block = webClient.baseUrl(SdCommon.STRUCTURE.getRequestUrl()).codecs(item -> item.defaultCodecs().maxInMemorySize(20 * 1024 * 1024)).build().post().uri(router).body(BodyInserters.fromValue(model)).retrieve().bodyToMono(String.class).block();
            //获取到指定图片 SD给的是BASE64数据
            final String base64 = JSONObject.parseObject(block).getJSONArray("images").getString(0);
            //上传至阿里云
            final String drawingUrl = uploadUtil.uploadBase64Image(base64, FileEnum.DRAWING.getDec());
            tsGenerateDrawing.setStatus(DrawingStatusConstant.SUCCEED).setUrl(drawingUrl);
            //设置构建成功
            redisTemplate.opsForValue().set(DrawingConstant.TASK + taskId, taskStructure.setImageUrl(drawingUrl).setStatus(DrawingStatusConstant.SUCCEED), 600, TimeUnit.SECONDS);

        } catch (Exception e) {
            tsExceptionMapper.insert(new TsException().setCause("在调用SD API时失败,请在nacos配置中心中检查配置 错误信息:" + e.getMessage()).setLevel(LevelEnum.HEIGHT.getDesc()).setServerName(ServerEnum.DRAWING.getDesc()));
            tsGenerateDrawing.setStatus(DrawingStatusConstant.DISUSE);
            //设置返回错误结果
            redisTemplate.opsForValue().set(DrawingConstant.TASK + taskId, taskStructure.setStatus(DrawingStatusConstant.DISUSE), 600, TimeUnit.SECONDS);
        } finally {
            //移除当前执行队列 进入空等待 释放资源
            redisTemplate.delete(DrawingConstant.SD_EXECUTION);
            tsGenerateDrawingMapper.updateById(tsGenerateDrawing);
        }

    }


}
