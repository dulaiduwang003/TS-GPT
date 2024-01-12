package com.cn.common.structure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * The type Dall structure.
 */
@Data
@Accessors(chain = true)
public class DallStructure implements Serializable {

    /**
     * The Request url.
     */
    private String requestUrl;

    /**
     * The Key list.
     */
    private List<String> keyList;

}
