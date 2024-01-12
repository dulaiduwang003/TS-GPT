package com.cn.controller.admin;

import com.cn.dto.AddDallKeyDto;
import com.cn.dto.DeleteDallKeyDto;
import com.cn.dto.SetDallExtraDto;
import com.cn.dto.UpdateDallKeyDto;
import com.cn.msg.Result;
import com.cn.service.DallConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * dall管理api
 */
@Slf4j
@RestController
@RequestMapping("/dall-management")
@RequiredArgsConstructor
public class DallManagementController {

    private final DallConfigService dallConfigService;


    /**
     * 获取DALL额外配置
     *
     * @return the dall key page
     */
    @GetMapping(value = "/get/dall-extra", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getDallExtraConfig() {
        return Result.data(dallConfigService.getDallExtraConfig());
    }


    /**
     * 获取DALL额外配置
     *
     * @return the dall key page
     */
    @PostMapping(value = "/set/dall-extra", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result setDallExtraConfig(@RequestBody @Validated final SetDallExtraDto dto) {
        dallConfigService.setDallExtraConfig(dto);
        return Result.ok();
    }


    /**
     * 获取dall密钥 分页
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the dall key page
     */
    @GetMapping(value = "/get/dall-key/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getDallKeyPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam String prompt) {
        return Result.data(dallConfigService.getDallKeyPage(pageNum, prompt));
    }

    /**
     * 修改dall密钥
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/update/dall-key", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateDallKey(@RequestBody @Validated UpdateDallKeyDto dto) {
        dallConfigService.updateDallKey(dto);
        return Result.ok();
    }

    /**
     * 删除dall密钥
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/delete/dall-key", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteDallKey(@RequestBody @Validated DeleteDallKeyDto dto) {
        dallConfigService.deleteDallKey(dto);
        return Result.ok();
    }

    /**
     * 新增dall密钥
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/add/dall-key", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result addDallKey(@RequestBody @Validated AddDallKeyDto dto) {
        dallConfigService.addDallKey(dto);
        return Result.ok();
    }


    /**
     * 加载至内存配置
     *
     * @return the result
     */
    @PostMapping(value = "/load/load-dall-structure", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result loadDallStructure() {
        dallConfigService.loadDallStructure();
        return Result.ok();
    }

}
