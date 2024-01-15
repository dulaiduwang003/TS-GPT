package com.cn.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * The type Model vo.
 */
@Accessors(chain = true)
@Getter
@Setter
public class ModelVo implements Serializable {

    /**
     * The Model index.
     */
    private Integer modelIndex;

    /**
     * The Model name.
     */
    private String modelName;

}
