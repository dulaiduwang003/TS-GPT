package com.cn.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.common.SdCommon;
import com.cn.constant.DrawingConstant;
import com.cn.constant.DrawingStatusConstant;
import com.cn.dto.AddDrawingPromptDto;
import com.cn.dto.DeleteDrawingPromptDto;
import com.cn.dto.GenerateDrawingDeleteDto;
import com.cn.dto.UpdateDrawingPromptDto;
import com.cn.entity.TsDrawingPrompt;
import com.cn.entity.TsGenerateDrawing;
import com.cn.enums.DrawingTypeEnum;
import com.cn.exception.DrawingException;
import com.cn.mapper.TsDrawingPromptMapper;
import com.cn.mapper.TsGenerateDrawingMapper;
import com.cn.service.DrawingService;
import com.cn.structure.TaskStructure;
import com.cn.utils.RedisUtils;
import com.cn.utils.StringUtils;
import com.cn.utils.UploadUtil;
import com.cn.utils.UserUtils;
import com.cn.vo.DrawingPromptPageVo;
import com.cn.vo.DrawingResultVo;
import com.cn.vo.PromptWordVo;
import com.cn.vo.TaskResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * The type Drawing service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DrawingServiceImpl implements DrawingService {

    /**
     * The Ts generate drawing mapper.
     */
    private final TsGenerateDrawingMapper tsGenerateDrawingMapper;

    /**
     * The Ts drawing prompt mapper.
     */
    private final TsDrawingPromptMapper tsDrawingPromptMapper;

    /**
     * The Redis utils.
     */
    private final RedisUtils redisUtils;

    /**
     * The Upload util.
     */
    private final UploadUtil uploadUtil;

    /**
     * Add drawing prompt.
     *
     * @param dto the dto
     */
    @Override
    public void addDrawingPrompt(AddDrawingPromptDto dto) {
        tsDrawingPromptMapper.insert(new TsDrawingPrompt()
                .setPrompt(dto.getPrompt())
                .setType(dto.getType())

        );
    }

    /**
     * Update drawing prompt.
     *
     * @param dto the dto
     */
    @Override
    public void updateDrawingPrompt(UpdateDrawingPromptDto dto) {
        tsDrawingPromptMapper.updateById(new TsDrawingPrompt()
                .setDrawingPromptId(dto.getDrawingPromptId())
                .setPrompt(dto.getPrompt())
                .setType(dto.getType())
        );
    }

    /**
     * Delete drawing prompt.
     *
     * @param dto the dto
     */
    @Override
    public void deleteDrawingPrompt(DeleteDrawingPromptDto dto) {
        tsDrawingPromptMapper.delete(new QueryWrapper<TsDrawingPrompt>()
                .lambda()
                .eq(TsDrawingPrompt::getDrawingPromptId, dto.getDrawingPromptId())
        );
    }

    /**
     * Ger drawing opus list.
     *
     * @return the list
     */
    @Override
    public List<DrawingResultVo> gerDrawingOpus() {
        return tsGenerateDrawingMapper.selectList(new QueryWrapper<TsGenerateDrawing>().lambda().eq(TsGenerateDrawing::getUserId, UserUtils.getCurrentLoginId()).select(TsGenerateDrawing::getPrompt, TsGenerateDrawing::getType, TsGenerateDrawing::getGenerateDrawingId, TsGenerateDrawing::getCreatedTime, TsGenerateDrawing::getStatus, TsGenerateDrawing::getUrl).orderByAsc(TsGenerateDrawing::getCreatedTime)).stream().map(t -> new DrawingResultVo().setStatus(t.getStatus()).setTaskId(t.getGenerateDrawingId()).setDrawingType(t.getType()).setPrompt(t.getPrompt()).setImage(t.getUrl()).setCreatedTime(t.getCreatedTime())).toList();
    }

    /**
     * Delete drawing opus by task id.
     *
     * @param dto the dto
     */
    @Override
    public void deleteDrawingOpusByTaskId(GenerateDrawingDeleteDto dto) {

        final TsGenerateDrawing tsGenerateDrawing = tsGenerateDrawingMapper.selectOne(new QueryWrapper<TsGenerateDrawing>()
                .lambda()
                .eq(TsGenerateDrawing::getGenerateDrawingId, dto.getTaskId())
                .select(TsGenerateDrawing::getUrl, TsGenerateDrawing::getStatus)
        );
        if (tsGenerateDrawing != null) {
            final String status = tsGenerateDrawing.getStatus();
            if (Objects.equals(status, DrawingStatusConstant.PENDING) || Objects.equals(status, DrawingStatusConstant.PROCESSING)) {
                throw new DrawingException("该任务正在等待被系统处理,请等待处理完毕后再删除");
            }
            //删除文件
            if (!status.equals(DrawingStatusConstant.DISUSE)) {
                uploadUtil.deletedFile(tsGenerateDrawing.getUrl());
            }

            tsGenerateDrawingMapper.delete(new QueryWrapper<TsGenerateDrawing>().lambda().eq(TsGenerateDrawing::getUserId, UserUtils.getCurrentLoginId()).eq(TsGenerateDrawing::getGenerateDrawingId, dto.getTaskId()));
        } else
            throw new DrawingException("任务不存在或已被系统自动删除");


    }

    /**
     * Gets random prompt word.
     *
     * @param type the type
     * @return the random prompt word
     */
    @Override
    public PromptWordVo getRandomPromptWord(final String type) {

        final TsDrawingPrompt tsDrawingPrompt = tsDrawingPromptMapper.selectOne(new QueryWrapper<TsDrawingPrompt>()
                .eq("type", type)
                .select("prompt")
                .orderByAsc("RAND()")
                .last("LIMIT 1")
        );
        if (tsDrawingPrompt != null) {
            return new PromptWordVo().setPrompt(tsDrawingPrompt.getPrompt());
        }
        return null;

    }

    /**
     * Gets drawing task.
     *
     * @param taskId the task id
     * @return the drawing task
     */
    @Override
    public TaskResultVo getDrawingTask(final String taskId) {
        String assetTaskId = DrawingConstant.TASK + taskId;
        if (redisUtils.doesItExist(assetTaskId)) {
            final TaskStructure taskStructure = (TaskStructure) redisUtils.getValue(assetTaskId);
            //判断任务来源
            if (taskStructure.getDrawingType().equals(DrawingTypeEnum.SD.getDec())) {
                return handleGetSdTask(taskStructure, taskId);
            } else
                return handleGetDallTask(taskStructure, taskId);

        }
        //任务已被抛弃 或不存在
        return null;
    }

    /**
     * Gets drawing prompt page.
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the drawing prompt page
     */
    @Override
    public IPage<DrawingPromptPageVo> getDrawingPromptPage(final int pageNum, final String prompt) {
        return tsDrawingPromptMapper.selectPage(new Page<>(pageNum, 15), new QueryWrapper<TsDrawingPrompt>()
                .lambda()
                .like(StringUtils.notEmpty(prompt), TsDrawingPrompt::getPrompt, prompt)
        ).convert(t -> new DrawingPromptPageVo()
                .setDrawingPromptId(t.getDrawingPromptId())
                .setPrompt(t.getPrompt())
                .setType(t.getType())
                .setCreatedTime(t.getCreatedTime())
                .setUpdateTime(t.getUpdateTime()));
    }

    /**
     * Handle get dall task task result vo.
     *
     * @param taskStructure the task structure
     * @param taskId        the task id
     * @return the task result vo
     */
    private TaskResultVo handleGetDallTask(final TaskStructure taskStructure, final String taskId) {
        //判断SD是否存在于链表
        final String handleTaskId = String.valueOf(redisUtils.hashGet(DrawingConstant.DALL_EXECUTION, taskId));
        if (taskId.equals(handleTaskId)) {
            return new TaskResultVo().setPrompt(taskStructure.getPrompt())
                    //预览效果
                    .setStatus(DrawingStatusConstant.PROCESSING);
        } else {
            return handleTaskStatus(taskStructure);
        }


    }


    /**
     * Handle get sd task task result vo.
     *
     * @param taskStructure the task structure
     * @param taskId        the task id
     * @return the task result vo
     */
    private TaskResultVo handleGetSdTask(TaskStructure taskStructure, String taskId) {
        //判断当前处理的是否为 当前task
        final String handleTaskId = String.valueOf(redisUtils.getValue(DrawingConstant.SD_EXECUTION));
        //验证状态
        if (taskId.equals(handleTaskId)) {
            //实时获取数据
            try {
                final JSONObject jsonObject = getProgressFromWebClient();
                //计算距离完成百分比
                int percentage = calculatePercentage(jsonObject);
                HashMap<String, Object> map = new HashMap<>();
                map.put("progress", percentage);
                map.put("current_image", jsonObject.getString("current_image"));
                return new TaskResultVo().setPrompt(taskStructure.getPrompt())
                        //预览效果
                        .setStatus(DrawingStatusConstant.PROCESSING).setExtra(map);
            } catch (Exception e) {
                return new TaskResultVo().setPrompt(taskStructure.getPrompt()).setStatus(DrawingStatusConstant.PROCESSING);
            }

        } else {
            return handleTaskStatus(taskStructure);
        }
    }

    /**
     * Gets progress from web client.
     *
     * @return the progress from web client
     */
    private JSONObject getProgressFromWebClient() {
        final String block = WebClient.builder().baseUrl(SdCommon.STRUCTURE.getRequestUrl()).codecs(item -> item.defaultCodecs().maxInMemorySize(20 * 1024 * 1024)).build().get().uri("/progress").header(HttpHeaders.CONNECTION, "Close").retrieve().bodyToMono(String.class).block();
        return JSONObject.parseObject(block);
    }

    /**
     * Calculate percentage int.
     *
     * @param jsonObject the json object
     * @return the int
     */
    private int calculatePercentage(JSONObject jsonObject) {
        final JSONObject state = jsonObject.getJSONObject("state");

        return (state.getInteger("sampling_step") * 100) / state.getInteger("sampling_steps");
    }

    /**
     * Handle task status task result vo.
     *
     * @param taskStructure the task structure
     * @return the task result vo
     */
    private TaskResultVo handleTaskStatus(final TaskStructure taskStructure) {
        final String status = taskStructure.getStatus();
        TaskResultVo taskResultVo = new TaskResultVo().setPrompt(taskStructure.getPrompt());
        if (status.equals(DrawingStatusConstant.PENDING)) {
            taskResultVo.setStatus(DrawingStatusConstant.PENDING);
        } else if (status.equals(DrawingStatusConstant.SUCCEED)) {
            //构建完毕
            taskResultVo.setStatus(DrawingStatusConstant.SUCCEED)
                    //清空数据集 释放内存空间 剩余默认 30分钟自动删除
                    .setExtra(null).setImage(taskStructure.getImageUrl());
        } else {
            taskResultVo.setStatus(DrawingStatusConstant.DISUSE).setExtra(null);
        }

        return taskResultVo;
    }
}
