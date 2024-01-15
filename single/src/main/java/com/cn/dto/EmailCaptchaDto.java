package com.cn.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取邮箱验证码DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class EmailCaptchaDto {

    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "注册邮箱不能为空")
    private String email;

}
