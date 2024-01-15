package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Micro category page vo.
 */
@Data
@Accessors(chain = true)
public class MicroCategoryPageVo implements Serializable {

    /**
     * The Micro category id.
     */
    private Long microCategoryId;

    /**
     * The Category name.
     */
    private String categoryName;

    /**
     * The El icon.
     */
    private String elIcon;

    /**
     * The Created time.
     */
    private LocalDateTime createdTime;

    /**
     * The Update time.
     */
    private LocalDateTime updateTime;

}
