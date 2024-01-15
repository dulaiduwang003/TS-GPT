package com.cn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 使用指定预设词模板 DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class UseMicroTemplateDto {

    @NotNull(message = "模板标识不能为空")
    private Integer microAppId;

}
