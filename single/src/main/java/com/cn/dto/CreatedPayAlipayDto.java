package com.cn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 创建支付宝订单DTO
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class CreatedPayAlipayDto {

    @NotNull(message = "商品标识不能为空")
    private Long productCardId;
}
