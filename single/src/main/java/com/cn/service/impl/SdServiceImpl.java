package com.cn.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.cn.common.SdCommon;
import com.cn.common.structure.SdStructure;
import com.cn.constant.DrawingConstant;
import com.cn.constant.DrawingStatusConstant;
import com.cn.dto.SdTaskDto;
import com.cn.entity.TsGenerateDrawing;
import com.cn.enums.DrawingTypeEnum;
import com.cn.mapper.TsGenerateDrawingMapper;
import com.cn.model.SdModel;
import com.cn.service.SdService;
import com.cn.structure.TaskStructure;
import com.cn.utils.DrawingUtils;
import com.cn.utils.RedisUtils;
import com.cn.utils.StringUtils;
import com.cn.utils.UserUtils;
import com.cn.vo.SdParamVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * The type Sd service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SdServiceImpl implements SdService {

    /**
     * The Redis template.
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * The Redis utils.
     */
    private final RedisUtils redisUtils;

    /**
     * The Drawing utils.
     */
    private final DrawingUtils drawingUtils;

    /**
     * The Ts generate drawing mapper.
     */
    private final TsGenerateDrawingMapper tsGenerateDrawingMapper;


    /**
     * Gets sd param.
     *
     * @return the sd param
     */
    @Override
    public SdParamVo getSdParam() {
        final SdStructure config = SdCommon.getConfig();
        return new SdParamVo()
                .setSamplerList(config.getSamplerList())
                .setSteps(config.getSteps())
                .setModelList(config.getModelList());
    }

    /**
     * Add sd drawing task string.
     *
     * @param dto the dto
     * @return the string
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addSdDrawingTask(final SdTaskDto dto) {
        //校验用户会员
        UserUtils.membershipHasExpiredErrorMsg(StpUtil.getLoginIdAsLong());
        final Long currentLoginId = drawingUtils.verifyTask();
        final String taskId = String.valueOf(UUID.randomUUID());
        final String actualTaskId = DrawingConstant.TASK + taskId;
        //设置个人任务队列
        final TaskStructure taskStructure = new TaskStructure()
                //设置任务状态为 构件中
                .setTaskId(taskId)
                .setStatus(DrawingStatusConstant.PENDING)
                .setDrawingType(DrawingTypeEnum.SD.getDec())
                .setPrompt(dto.getPrompt());
        //设置个人任务
        redisUtils.setValueTimeout(actualTaskId, taskStructure, 1800);
        //判断是否上传图片
        final SdModel sdModel = new SdModel()
                .setPrompt(dto.getPrompt())
                .setSteps(dto.getSteps())
                .setWidth(dto.getWidth())
                .setHeight(dto.getHeight())
                //设置动态模型昵称
                .setOverride_settings(
                        new SdModel.Override()
                                .setSd_model_checkpoint(dto.getModelName())
                                .setSd_vae(dto.getSd_vae())
                )
                .setSampler_index(dto.getSampler_index())
                .setMask(dto.getMask())
                .setNegative_prompt(dto.getNegative_prompt());
        if (StringUtils.notEmpty(dto.getImages())) {
            sdModel.setInit_images(List.of(dto.getImages()));
        }
        tsGenerateDrawingMapper.insert(new TsGenerateDrawing()
                .setStatus(DrawingStatusConstant.PENDING)
                .setPrompt(dto.getPrompt())
                .setGenerateDrawingId(taskId)
                .setType(DrawingTypeEnum.SD.getDec())
                .setUserId(currentLoginId)
        );
        redisTemplate.opsForList().leftPush(DrawingConstant.SD_TASK_QUEUE, taskStructure.setExtra(sdModel));

        return taskId;
    }


}
