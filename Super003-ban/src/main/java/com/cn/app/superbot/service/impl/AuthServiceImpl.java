package com.cn.app.superbot.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.RoleConstants;
import com.cn.app.superbot.constants.UserConstants;
import com.cn.app.superbot.dto.BackgroundLoginDto;
import com.cn.app.superbot.dto.WeChatLoginDto;
import com.cn.app.superbot.entity.MiniProgramUser;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.mapper.MiniProgramUserMapper;
import com.cn.app.superbot.model.UserModel;
import com.cn.app.superbot.service.AuthService;
import com.cn.app.superbot.utils.RedisUtils;
import com.cn.app.superbot.utils.WeChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * The type Auth service.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AuthServiceImpl extends ServiceImpl<MiniProgramUserMapper, MiniProgramUser> implements AuthService {


    /**
     * The Account.
     */
    @Value(value = "${console.account}")
    private String account;

    /**
     * The Password.
     */
    @Value(value = "${console.password}")
    private String password;

    /**
     * The Redis utils.
     */

    private final RedisUtils redisUtils;

    /**
     * The Mini program user mapper.
     */
    private final MiniProgramUserMapper miniProgramUserMapper;


    /**
     * The chat utils.
     */
    private final WeChatUtils weChatUtils;


    /**
     * We chat login string.
     *
     * @param dto the dto
     * @return the string
     */
    @Override
    public String weChatLogin(WeChatLoginDto dto) {
        final String openId = weChatUtils.getOpenId(dto);
        System.out.println(openId);
        MiniProgramUser _m = miniProgramUserMapper.selectOne(new QueryWrapper<MiniProgramUser>()
                .lambda()
                .eq(MiniProgramUser::getOpenId, openId)
                .select(MiniProgramUser::getMiniProgramUserId, MiniProgramUser::getOpenId)
        );
        /*
            If empty, indicates that the user is logged in for the first time
            INSET DB
         */
        if (_m == null) {
            _m = new MiniProgramUser().setOpenId(openId);
            miniProgramUserMapper.insert(_m);
        }
        // insert  user model cache
        redisUtils.hashPut(UserConstants.USER_INFO, _m.getMiniProgramUserId().toString(),
                new UserModel().setRole(RoleConstants.USER).setOpenId(openId));
        //login
        StpUtil.login(_m.getMiniProgramUserId());
        return StpUtil.getTokenValue();
    }


    /**
     * Server login string.
     *
     * @param dto the dto
     * @return the string
     */
    @Override
    public String serverLogin(final BackgroundLoginDto dto) {
        if (!(dto.getAccount().equals(account) && dto.getPassword().equals(password))) {
            throw new CustomException(MsgConstants.PWD_ERR, 400);

        }
        redisUtils.hashPut(UserConstants.USER_INFO, UserConstants.ADMINISTRATOR,
                new UserModel().setRole(RoleConstants.ADMIN));
        StpUtil.login(UserConstants.ADMINISTRATOR);
        return StpUtil.getTokenValue();
    }


    /**
     * We chat logout.
     */
    public void weChatLogout() {
        final boolean isLogin = StpUtil.isLogin();
        try {
            if (isLogin) {
                redisUtils.hashDelete(UserConstants.USER_INFO, StpUtil.getLoginId());
                StpUtil.logout();
            }
        } catch (Exception e) {
            throw new CustomException(MsgConstants.LOGOUT_ERR, 500);
        }
    }
}
