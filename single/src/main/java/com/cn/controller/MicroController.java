package com.cn.controller;

import com.cn.dto.UseMicroTemplateDto;
import com.cn.msg.Result;
import com.cn.service.MicroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提示词 API
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/micro")
@RequiredArgsConstructor
public class MicroController {

    private final MicroService microService;

    /**
     * 应用模板 (统计模型使用量)
     *
     * @param dto the dto
     * @return the micro applications list
     */
    @PostMapping(value = "/use/template", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result useMicroTemplate(@RequestBody @Validated UseMicroTemplateDto dto) {
        microService.useMicroApp(dto);
        return Result.ok();
    }

}
