
package com.cn.app.superbot.dto;

import com.cn.app.superbot.model.GptAlphaModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * The type Gpt alpha dto.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Data
public class GptAlphaDto {

    @NotBlank(message = "消息数据不能为空")
    private String prompt;

    /**
     * Convert to gpt alpha model gpt alpha model.
     *
     * @param item the item
     * @return the gpt alpha model
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static GptAlphaModel convertToGptAlphaModel(GptAlphaDto item) {
        if (item == null) {
            return null;
        }
        GptAlphaModel result = new GptAlphaModel();
        result.setPrompt(item.getPrompt());
        return result;
    }
}
