package com.cn.controller;


import com.cn.dto.EmailCaptchaDto;
import com.cn.exception.EmailException;
import com.cn.msg.Result;
import com.cn.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮箱类 API
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;


    /**
     * 发送验证码到指定邮箱
     *
     * @param dto the dto
     * @return the email code
     */
    @PostMapping(value = "/send/code",  produces = MediaType.APPLICATION_JSON_VALUE)
    public Result sendEmailCode(@RequestBody @Validated EmailCaptchaDto dto) {
        try {
            emailService.getEmailCode(dto.getEmail());
            return Result.ok();
        } catch (EmailException ex) {
            return Result.error(ex.getMessage());
        }
    }
}
