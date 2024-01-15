package com.cn.structure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type Task structure.
 */
@Data
@Accessors(chain = true)
public class TaskStructure implements Serializable {

    /**
     * The Drawing type.
     */
    private String drawingType;

    /**
     * The Task id.
     */
    private String taskId;

    /**
     * The Prompt.
     */
    private String prompt;

    /**
     * The Image url.
     */
    private String imageUrl;

    /**
     * The Status.
     */
    private String status;

    /**
     * The Extra.
     */
    private Object extra;

}
