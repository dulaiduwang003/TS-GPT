package com.cn.service.impl;

import com.cn.constant.EmailConstant;
import com.cn.exception.EmailException;
import com.cn.mapper.TsUserMapper;
import com.cn.service.EmailService;
import com.cn.utils.RedisUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;

/**
 * 邮箱业务实现
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value(value = "${spring.mail.username}")
    private String username;

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final TsUserMapper tsUserMapper;

    private final RedisUtils redisUtils;


    @Override
    public void getEmailCode(final String email) {
        final String code = RandomStringUtils.random(8, true, true).toUpperCase();
        Context context = new Context();
        context.setVariable("code", code);
        String process = templateEngine.process("emailCode.html", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject("TS GPT验证码");
            helper.setFrom(username);
            helper.setTo(email);
            helper.setSentDate(new Date());
            helper.setText(process, true);
            redisUtils.setValueTimeout(EmailConstant.CAPTCHA_CODE + email, code, 300);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("获取邮箱验证码失败 信息:{}, 位置", e.getMessage(), e.getClass());
            throw new EmailException("获取邮箱验证码失败!请稍后再试!", 500);
        } catch (MailSendException e) {
            throw new EmailException("您输入的邮箱账号不存在", 500);
        }
    }


}
