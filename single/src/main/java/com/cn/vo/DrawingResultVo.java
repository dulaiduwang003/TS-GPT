package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Drawing result vo.
 */
@Data
@Accessors(chain = true)
public class DrawingResultVo implements Serializable {

    /**
     * The Task id.
     */
    private String taskId;

    /**
     * The Image.
     */
    private String image;

    /**
     * The Status.
     */
    private String status;

    /**
     * The Prompt.
     */
    private String prompt;

    /**
     * The Drawing type.
     */
    private String drawingType;

    /**
     * The Created time.
     */
    private LocalDateTime createdTime;
}
