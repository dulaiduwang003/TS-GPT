package com.cn.structure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type User info structure.
 */
@Data
@Accessors(chain = true)
public class UserInfoStructure implements Serializable {

    /**
     * The Nick name.
     */
    private String nickName;

    /**
     * The Open id.
     */
    private String openId;

    /**
     * The Email.
     */
    private String email;

    /**
     * The Avatar.
     */
    private String avatar;

    /**
     * The Type.
     */
    private String type;

    /**
     * The Expiration time.
     */
    private LocalDateTime expirationTime;

}
