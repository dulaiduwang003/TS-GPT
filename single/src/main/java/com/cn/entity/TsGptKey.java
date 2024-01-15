package com.cn.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * gpt密钥表
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "ts_gpt_key")
@Accessors(chain = true)
public class TsGptKey {

    /**
     * 密钥id
     */
    @TableId(type = IdType.AUTO)
    private Long gptKeyId;

    /**
     * 密钥
     */
    private String openAiKey;

    /**
     * 作用域
     */
    private Integer isSeniorModel;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
