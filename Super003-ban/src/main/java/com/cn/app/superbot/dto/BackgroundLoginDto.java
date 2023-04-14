package com.cn.app.superbot.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Background login dto.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Getter
@Setter
@ToString
public class BackgroundLoginDto {

    /**
     * The Account.
     */
    @Size(min = 1, max = 20, message = "登录格式错误")
    private String account;

    /**
     * The Password.
     */
    @Size(min = 1, max = 20, message = "登陆密码格式错误")
    private String password;


}
