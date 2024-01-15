package com.cn.utils;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.cn.constant.ChatStatusConstant;
import com.cn.constant.UserCacheConstant;
import com.cn.exceptions.MemberException;
import com.cn.structure.UserInfoStructure;

import java.time.LocalDateTime;


/**
 * 用户工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
public class UserUtils {


    /**
     * Gets login id by token.
     *
     * @param token the token
     * @return the login id by token
     */
    public static Long getLoginIdByToken(final String token) {
        final Object loginIdObject = StpUtil.getLoginIdByToken(token);
        return Long.parseLong(String.valueOf(loginIdObject));
    }

    /**
     * Gets current login id.
     *
     * @return the current login id
     */
    public static Long getCurrentLoginId() {

        return StpUtil.getLoginIdAsLong();
    }

    public static void membershipHasExpiredCharacter(final Long userId) {
        final SaSession session = StpUtil.getSessionByLoginId(userId);
        //获取当前用户信息
        final UserInfoStructure userInfoStructure = (UserInfoStructure) session.get(UserCacheConstant.USER_INFO_DATA);
        //获取会员过期时间
        final LocalDateTime expirationTime = userInfoStructure.getExpirationTime();
        //获取系统当前时间
        final LocalDateTime currentTime = LocalDateTime.now();

        final boolean isMember = expirationTime != null && !expirationTime.isBefore(currentTime);
        if (!isMember) {
            throw new MemberException(ChatStatusConstant.OVERDUE_MEMBER);
        }

    }

    public static void membershipHasExpiredErrorMsg(final Long userId) {
        final SaSession session = StpUtil.getSessionByLoginId(userId);
        //获取当前用户信息
        final UserInfoStructure userInfoStructure = (UserInfoStructure) session.get(UserCacheConstant.USER_INFO_DATA);
        //获取会员过期时间
        final LocalDateTime expirationTime = userInfoStructure.getExpirationTime();
        //获取系统当前时间
        final LocalDateTime currentTime = LocalDateTime.now();

        final boolean isMember = expirationTime != null && !expirationTime.isBefore(currentTime);
        if (!isMember) {
            throw new MemberException("抱歉,您的会员已过期");
        }
    }



    /**
     * Gets current user info.
     *
     * @return the current user info
     */
    public static UserInfoStructure getCurrentUserInfo() {
        final Object o = StpUtil.getSession().get(UserCacheConstant.USER_INFO_DATA);
        return (UserInfoStructure) o;
    }

    /**
     * Gets current user info.
     *
     * @return the current user info
     */
    public static UserInfoStructure getUserInfoById(final Long userId) {
        final Object o = StpUtil.getSessionByLoginId(userId).get(UserCacheConstant.USER_INFO_DATA);
        return (UserInfoStructure) o;

    }


}
