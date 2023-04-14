/**
 * @author 明明不是下雨天
 */
package com.cn.supers.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;


/**
 * The type Gpt alpha model.
 */
@Data
@Accessors(chain = true)
public class ChatGPT$4Model implements Serializable {


    /**
     * The Model.
     */
    private String model = "gpt-4-0314";

    /**
     * The Top p.
     */
    private Double top_p = 0.9;


    /**
     * The Stream.
     */
    private boolean stream = true;

    /**
     * The Messages.
     */
    private List<Messages> messages;

    /**
     * The Max tokens.
     */
    private Integer max_tokens = 2048;


    /**
     * The type Messages.
     */
    @Data
    public static class Messages {

        /**
         * The Role.
         */
        private String role;

        /**
         * The Content.
         */
        private String content;
    }

}
