package com.cn.common;

import com.cn.common.structure.SdStructure;
import com.cn.configuration.SdDefaultConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * SD组件类
 */
@Component
@RequiredArgsConstructor
public class SdCommon {

    private final SdDefaultConfiguration configuration;

    public static final SdStructure STRUCTURE = new SdStructure();


    /**
     * 初始化.
     */
    @PostConstruct
    public void init() {
        STRUCTURE
                .setModelList(configuration.getModelList())
                .setSamplerList(configuration.getSamplerList())
                .setSteps(configuration.getSteps())
                .setRequestUrl(configuration.getRequestUrl());
    }

    /**
     * 获取配置
     *
     * @return the config
     */
    public static SdStructure getConfig() {
        return STRUCTURE;
    }

}
