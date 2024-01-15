package com.cn.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.entity.TsDialogueDrawing;
import com.cn.entity.TsGenerateDrawing;
import com.cn.mapper.TsDialogueDrawingMapper;
import com.cn.mapper.TsGenerateDrawingMapper;
import com.cn.utils.RedisUtils;
import com.cn.utils.UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.cn.constant.StatisticsConstant.*;

/**
 * auth定时器
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ScheduledTask {

    private final RedisUtils redisUtils;

    /**
     * The Upload util.
     */
    private final UploadUtil uploadUtil;

    private final TsDialogueDrawingMapper tsDialogueDrawingMapper;

    private final TsGenerateDrawingMapper tsGenerateDrawingMapper;


    /**
     * 刷新今日数据
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void refreshSiftData() {
        redisUtils.delKey(NEW_REVENUE);
        redisUtils.delKey(NEW_TRADE);
        redisUtils.delKey(NEW_USERS);
        redisUtils.delKey(NEW_VISITS);
        redisUtils.delKey(TOTAL_VISITS);
    }

    /**
     * 定时删除对话产生的图片
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void dialogueImageGenerationDeleteTask() {
        final List<TsDialogueDrawing> list = tsDialogueDrawingMapper.selectList(
                new QueryWrapper<TsDialogueDrawing>()
                        .lambda()
                        .ge(TsDialogueDrawing::getCreatedTime, LocalDateTime.now().minusDays(1))
                        .select(TsDialogueDrawing::getDialogueDrawingId, TsDialogueDrawing::getUrl)
        ).stream().toList();
        final List<Long> ids = list.stream().map(TsDialogueDrawing::getDialogueDrawingId).toList();
        if (!ids.isEmpty()) {
            //执行批量删除
            tsDialogueDrawingMapper.delete(new QueryWrapper<TsDialogueDrawing>().in("dialogue_drawing_id", ids));
            list.forEach(l -> uploadUtil.deletedFile(l.getUrl()));
        }
    }

    /**
     * 3天删除一次用户生成的图片
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void drawingImageGenerationDeleteTask() {
        final List<TsGenerateDrawing> list = tsGenerateDrawingMapper.selectList(
                new QueryWrapper<TsGenerateDrawing>()
                        .lambda()
                        .ge(TsGenerateDrawing::getCreatedTime, LocalDateTime.now().minusDays(3))
                        .select(TsGenerateDrawing::getGenerateDrawingId, TsGenerateDrawing::getUrl)
        ).stream().toList();
        final List<String> ids = list.stream().map(TsGenerateDrawing::getGenerateDrawingId).toList();
        if (!ids.isEmpty()) {
            //执行批量删除
            tsGenerateDrawingMapper.delete(new QueryWrapper<TsGenerateDrawing>().in("generate_drawing_id", ids));
            list.forEach(l -> uploadUtil.deletedFile(l.getUrl()));
        }
    }

}
