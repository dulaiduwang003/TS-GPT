package com.cn.controller;

import com.cn.dto.DallTaskDto;
import com.cn.dto.DialogueImageDto;
import com.cn.exception.DallException;
import com.cn.exceptions.MemberException;
import com.cn.msg.Result;
import com.cn.service.DallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * dall 控制器
 */
@Slf4j
@RestController
@RequestMapping("/dall")
@RequiredArgsConstructor
public class DallController {

    private final DallService dallService;


    /**
     * 发布对话生成图片任务
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/dialogue/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result pushDialogueTask(@RequestBody @Validated DialogueImageDto dto) {
        try {
            return Result.data(dallService.dialogGenerationImg(dto));
        } catch (DallException | MemberException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 发布DALL绘图任务
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/push/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result pushTask(@RequestBody @Validated DallTaskDto dto) {
        try {
            return Result.data(dallService.addDallTask(dto));
        } catch (DallException | MemberException e) {
            return Result.error(e.getMessage());
        }
    }

}
