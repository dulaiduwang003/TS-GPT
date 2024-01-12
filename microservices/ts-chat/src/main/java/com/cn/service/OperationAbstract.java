package com.cn.service;

import com.cn.enums.DialogueEnum;
import com.cn.service.impl.ChatGptServiceFlux;
import org.springframework.stereotype.Component;

/**
 * gpt对话抽象
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@SuppressWarnings("all")
public abstract class OperationAbstract<T> {

    /**
     * 根据类型提供对话数据流
     *
     * @param t            the t
     * @param dialogueEnum the dialogue enum
     * @return the flux
     */
    public ChatGptServiceFlux.FluxOutcome action(final T t, DialogueEnum dialogueEnum) {

        return switch (dialogueEnum) {
            //识别图片GPT
            case RECOGNITION -> this.streamImageConversations(t);
            //对话GPT
            case DIALOGUE -> this.streamConversations(t);
        };

    }

    /**
     * 对话GPT
     *
     * @param obj the obj
     * @return the chat gpt service flux . flux outcome
     */
    protected abstract ChatGptServiceFlux.FluxOutcome streamConversations(final T obj);

    /**
     * 识别图片GPT
     *
     * @param obj the obj
     * @return the chat gpt service flux . flux outcome
     */
    protected abstract ChatGptServiceFlux.FluxOutcome streamImageConversations(final T obj);

}
