package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新增商品DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AddProductCardDto {

    @NotBlank(message = "商品名称不能为空")
    private String productName;

    @NotNull(message = "所含天数不能为空")
    private Long days;

    @NotNull(message = "商品价格不能为空")
    private Double price;
}
