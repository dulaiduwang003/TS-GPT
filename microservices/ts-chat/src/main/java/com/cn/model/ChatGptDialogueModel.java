package com.cn.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * GPT对话模型请求类
 */
@Data
@Accessors(chain = true)
public class ChatGptDialogueModel implements Serializable {

    private String model;

    private Double top_p;

    private List<Messages> messages;

    private boolean stream = true;

    private Long max_tokens;

    private Double temperature;


    @Data
    public static class Messages {

        private String role;

        private String content;
    }


}
