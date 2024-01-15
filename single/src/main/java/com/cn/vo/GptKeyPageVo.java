package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * The type Gpt key page vo.
 */
@Data
@Accessors(chain = true)
public class GptKeyPageVo {

    /**
     * The Gpt key id.
     */
    private Long gptKeyId;

    /**
     * The Open ai key.
     */
    private String openAiKey;

    /**
     * The Is senior model.
     */
    private Boolean isSeniorModel;

    /**
     * The Created time.
     */
    private LocalDateTime createdTime;

    /**
     * The Update time.
     */
    private LocalDateTime updateTime;
}
