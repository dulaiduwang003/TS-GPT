package com.cn.controller;

import com.cn.dto.SdTaskDto;
import com.cn.exception.DrawingException;
import com.cn.exceptions.MemberException;
import com.cn.msg.Result;
import com.cn.service.SdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * SD 控制器
 */
@Slf4j
@RestController
@RequestMapping("/sd")
@RequiredArgsConstructor
public class SdController {

    private final SdService sdService;


    /**
     * 获取SD提交任务表单参数
     *
     * @return the sd param
     */
    @GetMapping(value = "/get/param", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getSdParam() {
        return Result.data(sdService.getSdParam());
    }


    /**
     * 发布SD绘图任务
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/push/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result pushGenerateTask(@RequestBody @Validated SdTaskDto dto) {
        try {
            return Result.data(sdService.addSdDrawingTask(dto));
        } catch (DrawingException | MemberException e) {
            return Result.error(e.getMessage());
        }
    }


}
