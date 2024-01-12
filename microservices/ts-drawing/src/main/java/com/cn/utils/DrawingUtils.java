package com.cn.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.common.PoolCommon;
import com.cn.constant.DrawingStatusConstant;
import com.cn.entity.TsGenerateDrawing;
import com.cn.exception.DrawingException;
import com.cn.mapper.TsGenerateDrawingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The type Drawing utils.
 */
@Component
@RequiredArgsConstructor
public class DrawingUtils {

    /**
     * The Ts generate drawing mapper.
     */
    private final TsGenerateDrawingMapper tsGenerateDrawingMapper;

    /**
     * Verify task long.
     *
     * @return the long
     */
    public Long verifyTask() {
        final Long currentLoginId = UserUtils.getCurrentLoginId();
        final Long taskCount = tsGenerateDrawingMapper.selectCount(new QueryWrapper<TsGenerateDrawing>()
                .lambda()
                .eq(TsGenerateDrawing::getUserId, currentLoginId)
                .eq(TsGenerateDrawing::getStatus, DrawingStatusConstant.PENDING)
                .or()
                .eq(TsGenerateDrawing::getStatus, DrawingStatusConstant.PROCESSING)
        );
        final Integer maximumTask = PoolCommon.STRUCTURE.getMaximumTask();
        if (taskCount >= maximumTask) {
            throw new DrawingException("目前，最多可以创建 " + maximumTask + " 任务");
        }
        return currentLoginId;
    }

}
