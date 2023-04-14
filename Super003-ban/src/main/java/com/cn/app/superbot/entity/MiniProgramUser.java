package com.cn.app.superbot.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * The type Mini program user.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "mini_program_user")
@Accessors(chain = true)
public class MiniProgramUser {

    /**
     * The Mini program user id.
     */
    @TableId(type = IdType.AUTO)
    private Long miniProgramUserId;

    /**
     * The Open id.
     */
    private String openId;

    /**
     * The Frequency.
     */
    private Long frequency;

    /**
     * The Share.
     */
    private Long share;

    /**
     * The Video.
     */
    private Long video;

    /**
     * The Del.
     */
    private Long del;


}
