package com.cn.controller.admin;

import com.cn.dto.*;
import com.cn.msg.Result;
import com.cn.service.MicroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 提示词管理
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/micro-management")
@RequiredArgsConstructor
public class MicroManagementController {

    private final MicroService microService;


    /**
     * 分页获取提示词
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the micro applications list
     */
    @GetMapping(value = "/get/micro-app/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getMicroAppPage(@RequestParam(defaultValue = "1") final int pageNum, @RequestParam final String prompt) {
        return Result.data(microService.getMicroAppPage(pageNum, prompt));
    }

    /**
     * 分页获取提示词类目
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the micro applications list
     */
    @GetMapping(value = "/get/micro-category/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getMicroCategoryPage(@RequestParam(defaultValue = "1") final int pageNum, @RequestParam final String prompt) {
        return Result.data(microService.getMicroCategory(pageNum, prompt));
    }

    /**
     * 新增一个提示词类目
     *
     * @param dto the dto
     * @return the micro applications list
     */
    @PostMapping(value = "/add/micro-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result addMicroCategory(@RequestBody @Validated AddMicroCategoryDto dto) {
        microService.addMicroCategory(dto);
        return Result.ok();
    }

    /**
     * 指定修改提示词类目
     *
     * @param dto the dto
     * @return the micro applications list
     */
    @PostMapping(value = "/update/micro-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateMicroCategory(@RequestBody @Validated UpdateMicroCategoryDto dto) {
        microService.updateMicroCategory(dto);
        return Result.ok();
    }


    /**
     * 指定删除提示词类目
     *
     * @param dto the dto
     * @return the micro applications list
     */
    @PostMapping(value = "/delete/micro-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteMicroCategory(@RequestBody @Validated DeleteMicroCategoryDto dto) {
        microService.deleteMicroCategory(dto);
        return Result.ok();
    }


    /**
     * 获取提示词类目列表
     *
     * @return the micro applications list
     */
    @GetMapping(value = "/get/micro-category/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getMicroCategoryList() {
        return Result.data(microService.getMicroCategoryList());
    }


    /**
     * 新增一个提示词
     *
     * @param dto the dto
     * @return the micro applications list
     */
    @PostMapping(value = "/add/micro-app", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result addMicroApp(@RequestBody @Validated AddMicroAppDto dto) {
        microService.addMicroApp(dto);
        return Result.ok();
    }

    /**
     * 指定删除一个提示词
     *
     * @param dto the dto
     * @return the micro applications list
     */
    @PostMapping(value = "/delete/micro-app", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteMicroApp(@RequestBody @Validated DeleteMicroAppDto dto) {
        microService.deleteMicroApp(dto);
        return Result.ok();
    }

    /**
     * 指定修改一个提示词
     *
     * @param dto the dto
     * @return the micro applications list
     */
    @PostMapping(value = "/update/micro-app", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateMicroApp(@RequestBody @Validated UpdateMicroAppDto dto) {
        microService.updateMicroApp(dto);
        return Result.ok();
    }
}
