package com.cn.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 绘图生成图片 表
 */
@Data
@TableName(value = "ts_generate_drawing")
@Accessors(chain = true)
public class TsGenerateDrawing {


    @TableId(type = IdType.INPUT)
    private String generateDrawingId;

    /**
     * 提示词
     */
    private String prompt;

    /**
     * 所属用户
     */
    private Long userId;

    /**
     * 图片生成状态
     */
    private String status;

    /**
     * 图片链接
     */
    private String url;

    /**
     * 图片类型
     */
    private String type;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
