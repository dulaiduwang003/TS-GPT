package com.cn.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * The type Thread pool executor properties.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolExecutorProperties {


    /**
     * 核心线程大小
     */
    private final Integer corePoolSize = 3;

    /**
     * 最大线程数
     */
    private final Integer maximumPoolSize = 3;

    /**
     * 存活时间
     */
    private final Long keepAliveTime = 15L;

}
