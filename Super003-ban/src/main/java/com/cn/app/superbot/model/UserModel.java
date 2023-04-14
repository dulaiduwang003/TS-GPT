package com.cn.app.superbot.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The type User model.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Data
@Accessors(chain = true)
public class UserModel{

    /**
     * The Open id.
     */
    private String openId;

    /**
     * The Role.
     */
    private String role;
}
