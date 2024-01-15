package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type Prompt word vo.
 */
@Data
@Accessors(chain = true)
public class PromptWordVo implements Serializable {

    /**
     * The Prompt.
     */
    private String prompt;
}
