package com.cn.app.superbot.dto;

import com.cn.app.superbot.model.GptFourModel;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * The type Gpt four dto.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Getter
@Setter
@ToString
public class GptFourDto {

    /**
     * The Messages.
     */
    @NotEmpty(message = "消息数据不能为空")
    private List<GptFourModel.Messages> messages;


    /**
     * Convert to gpt four model gpt four model.
     *
     * @param item the item
     * @return the gpt four model
     */
    public static GptFourModel convertToGptFourModel(GptFourDto item) {
        if (item == null) {
            return null;
        }
        GptFourModel result = new GptFourModel();
        result.setMessages(item.getMessages());
        return result;
    }
}
