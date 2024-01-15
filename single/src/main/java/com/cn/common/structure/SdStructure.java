package com.cn.common.structure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * SD配置结构体
 */
@Data
@Accessors(chain = true)
public class SdStructure implements Serializable {

    private String requestUrl;

    private List<SdModel> modelList;

    private Steps steps;

    private List<String> samplerList;

    @Data
    @Accessors(chain = true)
    public static class SdModel {

        private String modelName;

        private String modelText;
    }


    @Data
    @Accessors(chain = true)
    public static class Steps{

        private Integer min;

        private Integer max;
    }

}
