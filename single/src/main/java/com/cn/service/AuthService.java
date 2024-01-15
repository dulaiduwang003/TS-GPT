package com.cn.service;


import com.cn.dto.EmailLoginDto;

/**
 * 登录类 业务
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface AuthService {


    /**
     * 邮箱+验证码登录
     *
     * @param dto the dto
     * @return the string
     */
    String emailAuthLogin(final EmailLoginDto dto);

    /**
     * 微信扫码登录
     *
     * @param code the code
     * @return the string
     */
    String wechatAuthorizedLogin(final String code);


    /**
     * 退出登录.
     */
    void logout();
}
