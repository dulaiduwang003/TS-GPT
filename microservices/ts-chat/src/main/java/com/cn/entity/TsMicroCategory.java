package com.cn.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 预设词类目表
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "ts_micro_category")
@Accessors(chain = true)
public class TsMicroCategory {

    /**
     * 类目表id
     */
    @TableId(type = IdType.AUTO)
    private Long microCategoryId;

    /**
     * element plus 图标
     */
    private String elIcon;

    /**
     * 类目名称
     */
    private String categoryName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
