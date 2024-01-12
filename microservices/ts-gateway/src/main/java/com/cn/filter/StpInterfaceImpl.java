package com.cn.filter;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.cn.constant.UserCacheConstant;
import com.cn.structure.UserInfoStructure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * The type Stp interface.
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    /**
     * Gets permission list.
     *
     * @param loginId   the login id
     * @param loginType the login type
     * @return the permission list
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        return null;
    }

    /**
     * Gets role list.
     *
     * @param loginId   the login id
     * @param loginType the login type
     * @return the role list
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        try {
            UserInfoStructure structure = (UserInfoStructure) StpUtil.getSessionByLoginId(loginId).get(UserCacheConstant.USER_INFO_DATA);
            return Collections.singletonList(structure.getType());
        } catch (Exception e) {
            log.error("获取用户权限类型失败 信息:{}", e.getMessage());
            throw new RuntimeException();
        }

    }

}
