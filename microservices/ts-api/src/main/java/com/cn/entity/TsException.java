package com.cn.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * 异常信息实体
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "ts_exception")
@Accessors(chain = true)
public class TsException {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long exceptionId;
    /**
     * 服务名称
     */
    private String serverName;
    /**
     * 级别
     */
    private String level;
    /**
     * 描述
     */
    private String cause;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
