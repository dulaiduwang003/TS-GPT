package com.cn.vo;

import com.cn.common.structure.ChatGptStructure;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * The type Gpt extra vo.
 */
@Data
@Accessors(chain = true)
public class GptExtraVo implements Serializable {

    /**
     * The Request url.
     */
    private String requestUrl;

    /**
     * The Picture preview.
     */
    private ChatGptStructure.ImageRecognitionConfig picturePreview;


}
