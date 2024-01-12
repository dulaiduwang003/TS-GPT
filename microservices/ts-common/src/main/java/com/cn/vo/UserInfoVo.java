package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * The type User info vo.
 */
@Data
@Accessors(chain = true)
public class UserInfoVo {

    /**
     * The Nick name.
     */
    private String nickName;

    /**
     * The Avatar.
     */
    private String avatar;

    /**
     * The Type.
     */
    private String type;

    /**
     * The Member.
     */
    private Member member;

    /**
     * The type Member.
     */
    @Data
    @Accessors(chain = true)
    public static class Member {

        /**
         * The Is member.
         */
        private Boolean isMember;

        /**
         * The Expiration time.
         */
        private LocalDateTime expirationTime;

    }

}
