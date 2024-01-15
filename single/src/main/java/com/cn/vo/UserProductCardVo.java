package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 商品分页 (用户可见)
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class UserProductCardVo implements Serializable {

    /**
     * 商品ID
     */
    private Long productCardId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 所含天数
     */
    private Long days;

    /**
     * 是否选中
     */
    private Boolean isSelected = false;
}
