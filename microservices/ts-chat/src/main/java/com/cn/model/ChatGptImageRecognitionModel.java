package com.cn.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * GPT 识图模型请求类
 */
@Data
@Accessors(chain = true)
public class ChatGptImageRecognitionModel implements Serializable {

    private String model;

    private List<Messages> messages;

    private Integer max_tokens;


    @Data
    public static class Messages {

        private String role = "user";

        private List<Object> content;
    }


    @Data
    public static class issue {

        private String text;

        private String type = "user";
    }

    @Data
    public static class imageBase64 {

        private ImageUrl image_url;

        private String type = "image_url";
    }

    @Data
    public static class ImageUrl {

        private String url;

    }

}
