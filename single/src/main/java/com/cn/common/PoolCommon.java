package com.cn.common;

import com.cn.common.structure.PoolStructure;
import com.cn.configuration.PoolDefaultConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 池配置
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class PoolCommon {

    private final PoolDefaultConfiguration configuration;

    public static final PoolStructure STRUCTURE = new PoolStructure();

    /**
     * 初始化.
     */
    @PostConstruct
    public void init() {
        STRUCTURE
                .setDallConcurrent(configuration.getDallConcurrent())
                .setMaximumTask(configuration.getMaximumTask())
                .setSdConcurrent(configuration.getSdConcurrent());
    }


}
