package com.cn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@SuppressWarnings("all")
public class DeleteDrawingPromptDto {

    @NotNull(message = "删除标识不能为空")
    private Long drawingPromptId;

}
