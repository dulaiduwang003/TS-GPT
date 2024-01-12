package com.cn.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type Dialogue generate model.
 */
@Data
@Accessors(chain = true)
public class DialogueGenerateModel implements Serializable {

    /**
     * The Model.
     */
    private String model = "dall-e-3";

    /**
     * The Prompt.
     */
    private String prompt;

//    private String size = "512x512";
//
//    private String quality = "standard";
//
//    private Integer n = 1;
}
