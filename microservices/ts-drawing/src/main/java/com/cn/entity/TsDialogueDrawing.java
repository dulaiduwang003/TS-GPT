package com.cn.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 *  对话生成图 表
 */
@Data
@TableName(value = "ts_dialogue_drawing")
@Accessors(chain = true)
public class TsDialogueDrawing {

    @TableId(type = IdType.AUTO)
    private Long dialogueDrawingId;

    /**
     * 图片链接
     */
    private String url;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
