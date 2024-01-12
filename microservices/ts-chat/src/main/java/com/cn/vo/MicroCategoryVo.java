package com.cn.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type Micro category vo.
 */
@Data
@Accessors(chain = true)
public class MicroCategoryVo implements Serializable {

    /**
     * The Micro category id.
     */
    private Long microCategoryId;

    /**
     * The Category name.
     */
    private String categoryName;




}
