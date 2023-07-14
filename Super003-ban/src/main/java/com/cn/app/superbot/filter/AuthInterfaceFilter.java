package com.cn.app.superbot.filter;

import cn.dev33.satoken.stp.StpInterface;
import com.cn.app.superbot.constants.UserConstants;
import com.cn.app.superbot.model.UserModel;
import com.cn.app.superbot.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Auth interface.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Component
@RequiredArgsConstructor
public class AuthInterfaceFilter implements StpInterface {

    /**
     * The Redis utils.
     */
    private final RedisUtils redisUtils;

    /**
     * Gets role list.
     *
     * @param o the o
     * @param s the s
     * @return the role list
     */
    @Override
    public List<String> getRoleList(Object o, String s) {
        final UserModel userModel = (UserModel) redisUtils.hashGet(UserConstants.USER_INFO, o.toString());
        return List.of(userModel.getRole());
    }

    /**
     * Gets permission list.
     *
     * @param o the o
     * @param s the s
     * @return the permission list
     */
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }


}
