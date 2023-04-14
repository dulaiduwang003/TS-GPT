package com.cn.app.superbot.model;

import lombok.Data;

/**
 * The type Face model.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Data
public class FaceModel {

    /**
     * The Prompt.
     */
    private String prompt;
    /**
     * The Steps.
     */
    private Integer steps = 20;
}
