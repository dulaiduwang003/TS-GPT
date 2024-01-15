package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 邮箱+验证码登录DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class EmailLoginDto {

    @NotBlank(message = "登录邮箱不能为空")
    private String email;

    @NotBlank(message = "验证码不正确")
    private String code;
}
