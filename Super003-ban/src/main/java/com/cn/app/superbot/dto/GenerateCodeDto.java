package com.cn.app.superbot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenerateCodeDto {

    /**
     * The Frequency.
     */
    @NotNull(message = "兑换码次数不能为空")
    private Long frequency;

    /**
     * The Quantity.
     */
    @NotNull(message = "兑换码生成数量不能为空")
    private Long quantity;
}
