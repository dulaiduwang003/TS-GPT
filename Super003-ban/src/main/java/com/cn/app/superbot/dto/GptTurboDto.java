
package com.cn.app.superbot.dto;

import com.cn.app.superbot.model.GptTurboModel;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public class GptTurboDto {

    @NotEmpty(message = "消息数据不能为空")
    private List<GptTurboModel.Messages> messages;


    public static GptTurboModel convertToGptTurboModel(GptTurboDto item) {
        if (item == null) {
            return null;
        }
        GptTurboModel result = new GptTurboModel();
        result.setMessages(item.getMessages());
        return result;
    }
}
