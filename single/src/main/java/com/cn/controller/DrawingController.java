package com.cn.controller;

import com.cn.dto.GenerateDrawingDeleteDto;
import com.cn.exception.DrawingException;
import com.cn.msg.Result;
import com.cn.service.DrawingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 公有绘图控制器
 */
@Slf4j
@RestController
@RequestMapping("/drawing")
@RequiredArgsConstructor
public class DrawingController {


    private final DrawingService drawingService;


    /**
     * 删除个人作品
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/delete/opus", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteDrawingByTaskId(@RequestBody @Validated final GenerateDrawingDeleteDto dto) {
        try {
            drawingService.deleteDrawingOpusByTaskId(dto);
            return Result.ok();
        } catch (DrawingException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取我的作品集
     *
     * @return the all drawing
     */
    @GetMapping(value = "/get/opus", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getDrawingOpus() {
        return Result.data(drawingService.gerDrawingOpus());
    }


    /**
     * 获取指定任务 个人
     *
     * @param taskId the task id
     * @return the task
     */
    @GetMapping(value = "/get/task/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getTask(@PathVariable final String taskId) {
        return Result.data(drawingService.getDrawingTask(taskId));
    }


    /**
     * 根据类型随机获取提示词
     *
     * @param type the type
     * @return the prompt words randomly
     */
    @GetMapping(value = "/get/prompt/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getPromptWordsRandomly(@PathVariable final String type) {
        return Result.data(drawingService.getRandomPromptWord(type));
    }


}
