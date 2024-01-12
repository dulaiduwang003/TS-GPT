package com.cn.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 商品表
 *
 * @author 欧渐风 @github dulaiduwang003
 * @version 1.0
 */
@Data
@TableName(value = "ts_product_card")
@Accessors(chain = true)
public class TsProductCard {

    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long productCardId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品所含天数
     */
    private Long days;

    /**
     * 商品价格
     */
    private Double price;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
