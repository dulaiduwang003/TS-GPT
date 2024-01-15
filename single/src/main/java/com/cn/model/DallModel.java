package com.cn.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The type Dall model.
 */
@Data
@Accessors(chain = true)
@SuppressWarnings("all")
public class DallModel {

    /**
     * The Model.
     */
    private String model = "dall-e-2";

    /**
     * The Prompt.
     */
    private String prompt;

    /**
     * The Size.
     */
    private String size;

    /**
     * The Mask.
     */
    private String mask;

    /**
     * The Image.
     */
    private String image;

    /**
     * The N.
     */
    private Integer n = 1;

}
