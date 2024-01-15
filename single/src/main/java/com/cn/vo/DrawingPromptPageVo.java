package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Drawing prompt page vo.
 */
@Data
@Accessors(chain = true)
public class DrawingPromptPageVo implements Serializable {

    /**
     * The Drawing prompt id.
     */
    private Long drawingPromptId;

    /**
     * The Prompt.
     */
    private String prompt;

    /**
     * The Type.
     */
    private String type;

    /**
     * The Created time.
     */
    private LocalDateTime createdTime;

    /**
     * The Update time.
     */
    private LocalDateTime updateTime;
}
