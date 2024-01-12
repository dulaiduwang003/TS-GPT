package com.cn.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.cn.constant.UserCacheConstant;
import com.cn.structure.UserInfoStructure;


/**
 * 用户工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public class UserUtils {


    /**
     * 获取当前登录用户ID
     *
     * @return the current login id
     */
    public static Long getCurrentLoginId() {

        return StpUtil.getLoginIdAsLong();
    }


    /**
     * 获取当前登录用户信息
     *
     * @return the current user info
     */
    public static UserInfoStructure getCurrentUserInfo() {
        final Object o = StpUtil.getSession().get(UserCacheConstant.USER_INFO_DATA);
        return (UserInfoStructure) o;
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId the user id
     * @return the current user info
     */
    public static UserInfoStructure getUserInfoById(final Long userId) {
        final Object o = StpUtil.getSessionByLoginId(userId).get(UserCacheConstant.USER_INFO_DATA);
        return (UserInfoStructure) o;

    }


}
