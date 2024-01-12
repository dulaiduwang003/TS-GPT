package com.cn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 新增gpt密钥 DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AddGptKeyDto implements Serializable {


    @NotNull(message = "作用域不能为空")
    private Boolean isSeniorModel;

    @NotNull(message = "密钥不能为空")
    private String openAiKey;

}
