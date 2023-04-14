package com.cn.app.superbot.api;

import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.msg.Result;
import com.cn.app.superbot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {

    /**
     * The User service.
     */
    private final UserService userService;


    /**
     * Gets user info.
     *
     * @return the user info
     */
    @GetMapping(value = "/get/info", name = "get user information", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getUserInfo() {
        try {
            return Result.data(userService.getUserInfo());
        } catch (CustomException e) {
            log.error("failed to get user data");
            return Result.error(e.getMessage(), e.getCode());
        }

    }


    /**
     * Reward result.
     *
     * @return the result
     */
    @GetMapping(value = "/get/reward/{type}", name = "get rewards", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result reward(@PathVariable final Integer type) {
        try {
            return Result.data(userService.getRewards(type));
        } catch (CustomException e) {
            log.error("failed to send reward");
            return Result.error(e.getMessage(), e.getCode());
        }

    }


    /**
     * Exchange result.
     *
     * @param code the code
     * @return the result
     */
    @GetMapping(value = "/get/exchange/{code}", name = "exchange", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result exchange(@PathVariable final String code) {
        try {
            userService.useCode(code);
            return Result.ok(MsgConstants.EXCHANGE_CODE_SUCCESS);
        } catch (CustomException e) {
            log.error("exchange failed");
            return Result.error(e.getMessage(), e.getCode());
        }

    }

}
