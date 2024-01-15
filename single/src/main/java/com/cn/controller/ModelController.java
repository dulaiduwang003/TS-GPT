package com.cn.controller;

import com.cn.msg.Result;
import com.cn.service.ModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模型 API
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelController {


    private final ModelService modelService;


    /**
     * 获取gpt对话模型列表
     *
     * @return the result
     */
    @GetMapping(value = "/get/all", name = "获取模型列表", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result sendEmailCode() {
        return Result.data(modelService.getModelList());
    }

}
