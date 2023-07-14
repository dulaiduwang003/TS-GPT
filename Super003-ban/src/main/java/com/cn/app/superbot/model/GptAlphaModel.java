/**
 * @author 明明不是下雨天
 */
package com.cn.app.superbot.model;

import lombok.Data;


/**
 * The type Gpt alpha model.
 */
@Data
public class GptAlphaModel  {


    /**
     * The Model.
     */
    private String model = "image-alpha-001";


    /**
     * The Prompt.
     */
    private String prompt;


    /**
     * The Size.
     */
    private String size = "512x512";


    /**
     * The Num images.
     */
    private Integer num_images = 1;


    /**
     * The Response format.
     */
    private String response_format = "url";


}
