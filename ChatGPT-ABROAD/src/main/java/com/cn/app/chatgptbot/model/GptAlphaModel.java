/**
 * @author 明明不是下雨天
 */
package com.cn.app.chatgptbot.model;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * The type Gpt alpha model.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Data
@Accessors(chain = true)
public class GptAlphaModel {

    /**
     * model
     */
    private String model = "image-alpha-001";

    /**
     * message
     */
    private String prompt;

    /**
     * imageSize
     */
    private String size = "512x512";

    /**
     * numberOfImages
     */
    private Integer num_images = 1;

    /**
     * imageType
     */
    private String response_format = "url";


}
