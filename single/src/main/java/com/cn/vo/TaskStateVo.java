package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type Task state vo.
 */
@Data
@Accessors(chain = true)
public class TaskStateVo implements Serializable {

    /**
     * The Image base 64.
     */
    private String imageBase64;

    /**
     * The Expected be completed.
     */
    private String expectedBeCompleted;

}
