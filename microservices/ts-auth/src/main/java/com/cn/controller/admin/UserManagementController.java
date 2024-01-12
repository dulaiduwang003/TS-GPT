package com.cn.controller.admin;

import com.cn.dto.UpdateUserInfoDto;
import com.cn.exception.UserException;
import com.cn.msg.Result;
import com.cn.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理 api
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user-management")
@RequiredArgsConstructor
public class UserManagementController {


    private final UserService userService;



    /**
     * 分页获取用户信息
     *
     * @param pageNum the page num
     * @param prompt  the prompt
     * @return the user page
     */
    @GetMapping(value = "/get/user/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getUserPage(@RequestParam(defaultValue = "1") final int pageNum, @RequestParam final String prompt) {

        return Result.data(userService.getUserPage(pageNum, prompt));

    }


    /**
     * 修改指定用户信息
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/update/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateUser(@RequestBody @Validated UpdateUserInfoDto dto) {
        try {
            userService.updateUserInfo(dto);
            return Result.ok();
        } catch (UserException e) {
            return Result.error(e.getMessage());
        }

    }


}
