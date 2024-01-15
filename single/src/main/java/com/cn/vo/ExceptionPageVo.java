package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Site statistics vo.
 */
@Data
@Accessors(chain = true)
public class ExceptionPageVo implements Serializable {

    private Long exceptionId;

    private String serverName;

    private String level;

    private String cause;

    private LocalDateTime createdTime;

    private LocalDateTime updateTime;
}
