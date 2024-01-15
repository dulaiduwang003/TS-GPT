package com.cn.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 预设词表
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "ts_micro_app")
@Accessors(chain = true)
public class TsMicroApp {

    /**
     * 预设词id
     */
    @TableId(type = IdType.AUTO)
    private Long microAppId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 标题
     */
    private String title;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 中文问题模板
     */
    private String chineseIssue;

    /**
     * 英文问题模板
     */
    private String englishIssue;

    /**
     * 中文回答模板
     */
    private String chineseAnswer;

    /**
     * 英文回答模板
     */
    private String englishAnswer;

    /**
     * 所属预设词类目
     */
    private Long microCategoryId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
