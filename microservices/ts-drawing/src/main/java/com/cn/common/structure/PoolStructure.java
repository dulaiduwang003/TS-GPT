package com.cn.common.structure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type Pool structure.
 */
@Data
@Accessors(chain = true)
public class PoolStructure implements Serializable {

    /**
     * The Maximum task.
     */
    private Integer maximumTask;

    /**
     * The Sd concurrent.
     */
    private Integer sdConcurrent;

    /**
     * The Dall concurrent.
     */
    private Integer dallConcurrent;

}
