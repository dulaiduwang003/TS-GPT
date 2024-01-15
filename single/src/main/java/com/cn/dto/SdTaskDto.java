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
public class SdTaskDto {

    private String images;

    @NotBlank(message = "提示词不能为空")
    private String prompt;

    @NotNull(message = "图片宽度不能为空")
    private Long width;

    @NotNull(message = "图片高度不能为空")
    private Long height;

    @NotBlank(message = "模型名称不能为空")
    private String modelName;

    @NotNull(message = "迭代次数不能为空")
    private Integer steps;

    @NotBlank(message = "采样方法不能为空")
    private String sampler_index;

    @NotBlank(message = "外挂模型不能为空")
    private String sd_vae;

    private String negative_prompt;

    private String mask;

    private Double denoising_strength;


}
