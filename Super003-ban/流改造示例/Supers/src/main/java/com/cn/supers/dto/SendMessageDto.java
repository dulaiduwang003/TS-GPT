package com.cn.supers.dto;

import com.cn.supers.model.ChatGPT$4Model;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * The type Send message dto.
 */
@Data
@Accessors(chain = true)
public class SendMessageDto {


    private List<ChatGPT$4Model.Messages> messages;




}
