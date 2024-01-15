package com.cn.dto;

import com.cn.common.structure.ChatGptStructure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 设置gpt额外配置 DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class SetGptExtraDto implements Serializable {

    @NotBlank(message = "请求地址不能为空")
    private String requestUrl;

    @NotNull(message = "图片识别模型不能为空")
    private ChatGptStructure.ImageRecognitionConfig picturePreview;


}
