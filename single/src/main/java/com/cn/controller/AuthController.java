package com.cn.controller;

import com.cn.dto.EmailLoginDto;
import com.cn.exception.LoginException;
import com.cn.msg.Result;
import com.cn.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录类 api
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户邮箱登录接口
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/email/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result emailLogin(@RequestBody @Validated EmailLoginDto dto) {
        try {
            return Result.data(authService.emailAuthLogin(dto));
        } catch (LoginException ex) {
            return Result.error(ex.getMessage());
        }
    }


    /**
     * 用户注销身份信息接口
     *
     * @return the result
     */
    @PostMapping(value = "/sign/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result signOut() {
        authService.logout();
        return Result.ok();

    }

}
