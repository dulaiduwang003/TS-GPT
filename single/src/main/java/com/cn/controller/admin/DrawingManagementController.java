package com.cn.controller.admin;

import com.cn.dto.AddDrawingPromptDto;
import com.cn.dto.DeleteDrawingPromptDto;
import com.cn.dto.UpdateDrawingPromptDto;
import com.cn.msg.Result;
import com.cn.service.DrawingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 绘图管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/drawing-management")
@RequiredArgsConstructor
public class DrawingManagementController {

    private final DrawingService drawingService;


    /**
     * 分页获取提示词数据
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the dall key page
     */
    @GetMapping(value = "/get/drawing-prompt/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getDrawingPromptPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam String prompt) {
        return Result.data(drawingService.getDrawingPromptPage(pageNum, prompt));
    }

    /**
     * 修改指定提示词数据
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/update/drawing-prompt", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateDrawingPrompt(@RequestBody @Validated UpdateDrawingPromptDto dto) {
        drawingService.updateDrawingPrompt(dto);
        return Result.ok();
    }

    /**
     * 删除指定提示词
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/delete/drawing-prompt", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteDrawingPrompt(@RequestBody @Validated DeleteDrawingPromptDto dto) {
        drawingService.deleteDrawingPrompt(dto);
        return Result.ok();
    }


    /**
     * 修改指定提示词数据
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/add/drawing-prompt", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result addDrawingPrompt(@RequestBody @Validated AddDrawingPromptDto dto) {
        drawingService.addDrawingPrompt(dto);
        return Result.ok();
    }

}
