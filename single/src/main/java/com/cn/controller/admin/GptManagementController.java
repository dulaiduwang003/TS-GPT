package com.cn.controller.admin;

import com.cn.dto.*;
import com.cn.msg.Result;
import com.cn.service.GptConfigService;
import com.cn.service.GptKeyConfigService;
import com.cn.service.GptModelConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * gpt配置管理
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/gpt-management")
@RequiredArgsConstructor
public class GptManagementController {

    private final GptKeyConfigService gptKeyConfigService;

    private final GptConfigService gptConfigService;

    private final GptModelConfigService gptModelConfigService;


    /**
     * 设置额外配置
     *
     * @param dto the dto
     * @return the gpt extra config
     */
    @PostMapping(value = "/set/gpt/extra", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result setGptExtraConfig(@RequestBody @Validated final SetGptExtraDto dto) {
        gptConfigService.setGptExtraConfig(dto);
        return Result.ok();
    }


    /**
     * 获取额外配置参数
     *
     * @return the gpt extra config
     */
    @GetMapping(value = "/get/gpt/extra", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getGptExtraConfig() {

        return Result.data(gptConfigService.getGptExtraConfig());
    }


    /**
     * 分页获取gpt密钥
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the gpt key page
     */
    @GetMapping(value = "/get/gpt-key/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getGptKeyPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam String prompt) {
        return Result.data(gptKeyConfigService.getGptKeyPage(pageNum, prompt));
    }

    /**
     * 指定修改gpt密钥
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/update/gpt-key", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateGptKey(@RequestBody @Validated UpdateGptKeyDto dto) {
        gptKeyConfigService.updateGptKey(dto);
        return Result.ok();
    }

    /**
     * 指定删除gpt密钥
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/delete/gpt-key", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteGptKey(@RequestBody @Validated DeleteGptKeyDto dto) {
        gptKeyConfigService.deleteGptKey(dto);
        return Result.ok();
    }

    /**
     * 新增一个gpt密钥
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/add/gpt-key", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result addGptKey(@RequestBody @Validated AddGptKeyDto dto) {
        gptKeyConfigService.addGptKey(dto);
        return Result.ok();
    }


    /**
     * 获取gpt模型列表
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the gpt key page
     */
    @GetMapping(value = "/get/gpt-model/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getGptModelPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam String prompt) {
        return Result.data(gptModelConfigService.getGptModelPage(pageNum, prompt));
    }

    /**
     * 指定修改gpt模型
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/update/gpt-model", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateGptModel(@RequestBody @Validated UpdateGptModelDto dto) {
        gptModelConfigService.updateGptModel(dto);
        return Result.ok();
    }

    /**
     * 指定删除一个gpt模型
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/delete/gpt-model", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteGptModel(@RequestBody @Validated DeleteGptModelDto dto) {
        gptModelConfigService.deleteGptModel(dto);
        return Result.ok();
    }


    /**
     * 新增一个gpt模型
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/add/gpt-model", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result addGptModel(@RequestBody @Validated AddGptModelDto dto) {
        gptModelConfigService.addGptModel(dto);
        return Result.ok();
    }


    /**
     * 将配置载入至服务器内存中
     *
     * @return the result
     */
    @PostMapping(value = "/load/load-gpt-structure", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result loadGptStructure() {
        gptConfigService.loadGptStructure();
        return Result.ok();
    }
}
