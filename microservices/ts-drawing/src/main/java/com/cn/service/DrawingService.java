package com.cn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.dto.AddDrawingPromptDto;
import com.cn.dto.DeleteDrawingPromptDto;
import com.cn.dto.GenerateDrawingDeleteDto;
import com.cn.dto.UpdateDrawingPromptDto;
import com.cn.vo.DrawingPromptPageVo;
import com.cn.vo.DrawingResultVo;
import com.cn.vo.PromptWordVo;
import com.cn.vo.TaskResultVo;

import java.util.List;

/**
 * 通用绘图
 */
public interface DrawingService {

    /**
     * 获取个人作品
     *
     * @return the drawing result list
     */
    List<DrawingResultVo> gerDrawingOpus();


    /**
     * 获取指定绘图任务状态
     *
     * @param taskId the task id
     * @return the drawing task
     */
    TaskResultVo getDrawingTask(String taskId);


    /**
     * 根据绘图类型获取提示词
     *
     * @param type the type
     * @return the random prompt word
     */
    PromptWordVo getRandomPromptWord(String type);


    /**
     * 根据任务ID删除作品
     *
     * @param dto the dto
     */
    void deleteDrawingOpusByTaskId(final GenerateDrawingDeleteDto dto);


    /**
     * 获取绘图提示词分页
     */
    IPage<DrawingPromptPageVo> getDrawingPromptPage(final int pageNum, final String prompt);


    /**
     * 新增绘图提示词
     *
     * @param dto the dto
     */
    void addDrawingPrompt(final AddDrawingPromptDto dto);


    /**
     * 修改绘图提示词
     *
     * @param dto the dto
     */
    void updateDrawingPrompt(final UpdateDrawingPromptDto dto);


    /**
     * 删除绘图提示词
     *
     * @param dto the dto
     */
    void deleteDrawingPrompt(final DeleteDrawingPromptDto dto);
}
