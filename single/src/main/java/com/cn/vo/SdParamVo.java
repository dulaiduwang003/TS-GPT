package com.cn.vo;

import com.cn.common.structure.SdStructure;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * The type Sd param vo.
 */
@Data
@Accessors(chain = true)
public class SdParamVo implements Serializable {

    /**
     * The Model list.
     */
    private List<SdStructure.SdModel> modelList;

    /**
     * The Sampler list.
     */
    private List<String> samplerList;

    /**
     * The Steps.
     */
    private SdStructure.Steps steps;


}
