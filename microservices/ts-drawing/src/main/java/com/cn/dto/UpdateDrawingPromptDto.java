package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@SuppressWarnings("all")
public class UpdateDrawingPromptDto {

    @NotNull(message = "修改标识不能为空")
    private Long drawingPromptId;

    @NotBlank(message = "提示词不能为空")
    private String prompt;

    @NotBlank(message = "提示词所属类型不能为空")
    private String type;
}
