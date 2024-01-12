package com.cn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@SuppressWarnings("all")
public class DeleteDallKeyDto {

    @NotNull(message = "标识不能为空")
    private Long dallKeyId;

}
