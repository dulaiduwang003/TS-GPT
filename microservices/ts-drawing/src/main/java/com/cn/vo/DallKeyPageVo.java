package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Dall key page vo.
 */
@Data
@Accessors(chain = true)
public class DallKeyPageVo implements Serializable {

    /**
     * The Dall key id.
     */
    private Long dallKeyId;

    /**
     * The Open ai key.
     */
    private String openAiKey;

    /**
     * The Created time.
     */
    private LocalDateTime createdTime;

    /**
     * The Update time.
     */
    private LocalDateTime updateTime;
}
