
package com.cn.app.chatgptbot.dto;

import com.cn.app.chatgptbot.model.GptTurboModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * The type Gpt turbo dto.
 *
 * @author bdth
 * @email 2074055628@qq.com
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public final class GptTurboDto {

    /**
     * OpenId (WeChat)
     */
    @NotBlank(message = "code错误")
    private String openId;

    /**
     * whether it is streaming data
     */
    private boolean isStream;

    /**
     * messageData
     */
    @NotEmpty(message = "消息数据不能为空")
    private List<GptTurboModel.Messages> messages;


    /**
     * Convert to gpt turbo model gpt turbo model.
     *
     * @param item the item
     * @return the gpt turbo model
     */
    public static GptTurboModel convertToGptTurboModel(GptTurboDto item) {
        if (item == null) {
            return null;
        }
        return new GptTurboModel().setMessages(item.getMessages());
    }
}
