package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type Task result vo.
 */
@Data
@Accessors(chain = true)
public class TaskResultVo implements Serializable {

    /**
     * The Status.
     */
    private String status;

    /**
     * The Image.
     */
    private String image;

    /**
     * The Prompt.
     */
    private String prompt;

    /**
     * The Extra.
     */
    private Object extra;


}
