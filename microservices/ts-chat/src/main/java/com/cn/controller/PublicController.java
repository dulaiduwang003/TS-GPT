package com.cn.controller;

import com.cn.msg.Result;
import com.cn.service.MicroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公开访问 API
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final MicroService microService;

    /**
     * 获取提示词+类目 渲染接口 分页
     *
     * @param pageNum the page num
     * @return the micro applications list
     */
    @GetMapping(value = "/get/micro/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getMicroApplicationsPage(@RequestParam(defaultValue = "1") final int pageNum) {
        return Result.data(microService.getMicroAppPage(pageNum));
    }


    /**
     * 搜索提示词+类目 渲染接口 分页
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the result
     */
    @GetMapping(value = "/search/micro/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result searchMicroAppPage(@RequestParam(defaultValue = "1") final int pageNum, @RequestParam String prompt) {
        return Result.data(microService.searchMicroApp(prompt, pageNum));
    }
}
