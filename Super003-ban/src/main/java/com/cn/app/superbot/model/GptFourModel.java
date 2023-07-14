/**
 * @author 明明不是下雨天
 */
package com.cn.app.superbot.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * The type Gpt alpha model.
 */
@Data
@Accessors(chain = true)
public class GptFourModel {


    /**
     * The Model.
     */
    private String model = "gpt-4-0314";

    /**
     * The Top p.
     */
    private Double top_p = 0.9;

    /**
     * The Messages.
     */
    private List<Messages> messages;

    private boolean stream = false;

    /**
     * The Max tokens.
     */
    private Integer max_tokens = 2048;


    /**
     * The type Messages.
     *
     * @author bdth
     * @email 2074055628 @qq.om
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
