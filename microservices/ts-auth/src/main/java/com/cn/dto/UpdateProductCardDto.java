package com.cn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 修改商品DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class UpdateProductCardDto {

    @NotNull(message = "修改标识不能为空")
    private Long productCardId;

    @NotBlank(message = "商品名称不能为空")
    private String productName;


    @NotNull(message = "所含天数不能为空")
    private Long days;

    @NotNull(message = "商品价格不能为空")
    private Double price;
}
