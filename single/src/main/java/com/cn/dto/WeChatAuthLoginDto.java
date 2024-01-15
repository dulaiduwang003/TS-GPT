package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录 DTO
 *
 * @author 欧渐风 @github dulaiduwang003
 * @version 1.0
 */
@Data
public class WeChatAuthLoginDto {

    @NotBlank(message = "微信授权码不能为空")
    private String code;
}
