package com.cn.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 池配置模板
 */
@Configuration
@ConfigurationProperties(prefix = "config.pool")
@Data
public class PoolDefaultConfiguration {


    /**
     * 最大任务数
     */
    private Integer maximumTask;

    /**
     * sd并发任务
     */
    private Integer sdConcurrent;

    /**
     * dall并发任务
     */
    private Integer dallConcurrent;

}
