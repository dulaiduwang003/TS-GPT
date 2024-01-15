package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class GenerateDrawingDeleteDto {

    @NotBlank(message = "删除图片索引不能为空")
    private String taskId;


}
