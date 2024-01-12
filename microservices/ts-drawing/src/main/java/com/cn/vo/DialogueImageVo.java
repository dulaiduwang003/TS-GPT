package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The type Dialogue image vo.
 */
@Data
@Accessors(chain = true)
public class DialogueImageVo {

    /**
     * The Revised prompt.
     */
    private String revisedPrompt;


    /**
     * The Url.
     */
    private String url;
}
