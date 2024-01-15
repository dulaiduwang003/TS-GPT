package com.cn.configuration;

import com.cn.common.structure.SdStructure;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * SD配置类
 */
@Configuration
@ConfigurationProperties(prefix = "config.sd")
@Data
public class SdDefaultConfiguration {

    private String requestUrl;

    private List<SdStructure.SdModel> modelList;

    private List<String> samplerList;

    private SdStructure.Steps steps;

}
