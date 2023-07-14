package com.cn.app.superbot.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;


/**
 * The type Gpt alpha model.
 */
@Data
@Accessors(chain = true)
public class GptImageModel {


    /**
     * The File.
     */
    private MultipartFile file;

}
