package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 新增gpt模型 DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AddGptModelDto implements Serializable {


    @NotNull(message = "作用域不能为空")
    private Boolean isSeniorModel;

    @NotBlank(message = "模型名称不能为空")
    private String modelName;

    @NotNull(message = "top_p不能为空")
    private Double topP;

    @NotNull(message = "max_tokens不能为空")
    private Long maxTokens;

    @NotNull(message = "temperature不能为空")
    private Double temperature;
}
