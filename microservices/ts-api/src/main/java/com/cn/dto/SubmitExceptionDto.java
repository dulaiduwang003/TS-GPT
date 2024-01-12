package com.cn.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 提交异常DTO
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class SubmitExceptionDto implements Serializable {

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 级别
     */
    private String level;

    /**
     * 描述
     */
    private String cause;

}
