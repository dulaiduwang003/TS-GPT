package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 添加图生图
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@SuppressWarnings("all")
public class DallTaskDto {

    @NotBlank(message = "提示词不能为空")
    private String prompt;

    @NotNull(message = "图片大小不能为空")
    private String size;

    private String mask;

    private String image;
}
