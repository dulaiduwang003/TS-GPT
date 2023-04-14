package com.cn.app.superbot.api;

import cn.dev33.satoken.stp.StpUtil;
import com.cn.app.superbot.dto.BackgroundLoginDto;
import com.cn.app.superbot.dto.WeChatLoginDto;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.msg.Result;
import com.cn.app.superbot.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The type Auth api.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {


    /**
     * The Auth service.
     */
    private final AuthService authService;

    /**
     * We chat login result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/wx/login", name = "weChat authorized login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result weChatLogin(@RequestBody @Validated WeChatLoginDto dto) {
        try {
            return Result.data(authService.weChatLogin(dto));
        } catch (CustomException e) {
            log.error("weChat code:{} -> login failed ", dto.getCode());
            return Result.error(e.getMessage(), e.getCode());
        }
    }

    /**
     * Server login result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/server/login", name = "background login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result serverLogin(@RequestBody @Validated BackgroundLoginDto dto) {
        try {
            return Result.data(authService.serverLogin(dto));
        } catch (CustomException e) {
            log.error("weChat code:{} -> login failed ", dto.getAccount());
            return Result.error(e.getMessage(), e.getCode());
        }
    }

    /**
     * We chat logout result.
     *
     * @return the result
     */
    @PostMapping(value = "/wx/logout", name = "sign out", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result weChatLogout() {
        try {
            authService.weChatLogout();
            return Result.ok();
        } catch (CustomException e) {
            log.error("wechat user logoff failed TOKEN->{}", StpUtil.getTokenValue());
            return Result.error(e.getMessage(), e.getCode());
        }

    }
}
