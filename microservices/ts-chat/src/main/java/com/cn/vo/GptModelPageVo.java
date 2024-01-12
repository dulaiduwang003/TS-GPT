package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * The type Gpt model page vo.
 */
@Data
@Accessors(chain = true)
public class GptModelPageVo {

    /**
     * The Gpt model id.
     */
    private Long gptModelId;

    /**
     * The Model name.
     */
    private String modelName;

    /**
     * The Is senior model.
     */
    private Boolean isSeniorModel;

    /**
     * The Temperature.
     */
    private Double temperature;

    /**
     * The Top p.
     */
    private Double topP;

    /**
     * The Max tokens.
     */
    private Long maxTokens;

    /**
     * The Created time.
     */
    private LocalDateTime createdTime;

    /**
     * The Update time.
     */
    private LocalDateTime updateTime;
}
