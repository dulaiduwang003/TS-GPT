package com.cn.app.superbot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.app.superbot.dto.BackgroundLoginDto;
import com.cn.app.superbot.dto.WeChatLoginDto;
import com.cn.app.superbot.entity.MiniProgramUser;

/**
 * The interface Mini program user service.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public interface AuthService extends IService<MiniProgramUser> {


    /**
     * We chat login string.
     *
     * @param dto the dto
     * @return the string
     */
    String weChatLogin(final WeChatLoginDto dto);


    /**
     * Server login string.
     *
     * @param dto the dto
     * @return the string
     */
    String serverLogin(final BackgroundLoginDto dto);


    /**
     * We chat logout.
     */
    void weChatLogout();


}
