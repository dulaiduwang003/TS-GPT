package com.cn.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * The type Ts exception.
 */
@Data
@TableName(value = "ts_exception")
@Accessors(chain = true)
public class TsException {

    /**
     * The Exception id.
     */
    @TableId(type = IdType.AUTO)
    private Long exceptionId;

    /**
     * The Server name.
     */
    private String serverName;

    /**
     * The Level.
     */
    private String level;

    /**
     * The Cause.
     */
    private String cause;

    /**
     * The Created time.
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * The Update time.
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
