package com.cn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 删除异常信息DTO
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class DeleteExceptionDto {

    @NotNull(message = "标识不能为空")
    private Long exceptionId;

}
