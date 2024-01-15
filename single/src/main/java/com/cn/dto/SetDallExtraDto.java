package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SetDallExtraDto {

    @NotBlank(message = "请求链不能为空")
    private String requestUrl;
}
