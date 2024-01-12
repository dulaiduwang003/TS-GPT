package com.cn.service;


/**
 * 邮箱业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface EmailService {


    /**
     * 获取邮箱验证码
     *
     * @param email the email
     */
    void getEmailCode(final String email);


}
