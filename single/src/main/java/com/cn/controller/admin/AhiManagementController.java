package com.cn.controller.admin;

import com.cn.dto.DeleteExceptionDto;
import com.cn.msg.Result;
import com.cn.service.AhiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 终端 API
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/ahi-management")
@RequiredArgsConstructor
public class AhiManagementController {

    private final AhiService ahiService;

    /**
     * 获取站点数据
     *
     * @return the site data
     */
    @GetMapping(value = "/get/site-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getSiteData() {
        return Result.data(ahiService.getSiteStatistics());
    }

    /**
     * 删除指定异常信息
     *
     * @return the site data
     */
    @PostMapping(value = "/delete/exception", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteException(@RequestBody @Validated DeleteExceptionDto dto) {
        ahiService.deleteException(dto);
        return Result.ok();
    }

    /**
     * 分页获取异常信息
     *
     * @return the site data
     */
    @GetMapping(value = "/get/exception/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getExceptionPage(@RequestParam(defaultValue = "1") final int pageNum) {
        return Result.data(ahiService.getExceptionPage(pageNum));
    }

}
