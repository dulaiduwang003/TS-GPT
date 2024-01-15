package com.cn.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author 欧渐风 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "ts_user")
@Accessors(chain = true)
public class TsUser {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 微信标识
     */
    private String openId;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户会员过期时间
     */
    private LocalDateTime expirationTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
