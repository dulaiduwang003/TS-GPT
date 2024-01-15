package com.cn.common.structure;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * gpt调用信息缓存结构
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class ChatGptStructure implements Serializable {

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 低级模型列表
     */
    private List<String> lowLevelKeyList;

    /**
     * 高级模型列表
     */
    private List<String> seniorKeyList;

    /**
     * 识图GPT配置
     */
    private ImageRecognitionConfig imageRecognitionConfig;

    /**
     * 模型列表
     */
    private List<Model> modelList;


    /**
     * 模型
     */
    @Data
    @Accessors(chain = true)
    public static class Model {

        private String modelName;

        private Boolean isSeniorModel;

        private Double top_p;

        private Long max_tokens;

        private Double temperature;
    }

    /**
     *图像识别配置
     */
    @Data
    @Accessors(chain = true)
    public static class ImageRecognitionConfig {

        private String model;

        private String detail;

        private Integer max_tokens;
    }


}
