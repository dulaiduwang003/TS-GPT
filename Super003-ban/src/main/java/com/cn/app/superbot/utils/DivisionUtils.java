package com.cn.app.superbot.utils;

import com.cn.app.superbot.dto.GptFourDto;
import com.cn.app.superbot.dto.GptTurboDto;
import com.cn.app.superbot.model.GptFourModel;
import com.cn.app.superbot.model.GptTurboModel;

import java.util.List;

/**
 * The type Division utils.
 */
public class DivisionUtils {

    /**
     * Chat gpt turbo dto.
     *
     * @param dto the dto
     * @return the gpt turbo dto
     */
    public static GptTurboDto turboDivision(GptTurboDto dto) {
        final List<GptTurboModel.Messages> messages = dto.getMessages();
        if (messages.size() > 5) {
            messages.remove(0);
        }

        dto.setMessages(messages);
        return dto;
    }

    /**
     * Full division gpt four dto.
     *
     * @param dto the dto
     * @return the gpt four dto
     */
    public static GptFourDto fullDivision(GptFourDto dto) {
        final List<GptFourModel.Messages> messages = dto.getMessages();
        if (messages.size() > 5) {
            messages.remove(0);
        }
        dto.setMessages(messages);
        return dto;
    }

}
