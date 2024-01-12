package com.cn.configuration;


import com.cn.properties.ThreadPoolExecutorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Thread pool auto configuration.
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolExecutorProperties.class)
@ConditionalOnProperty(prefix = "thread.pool", name = "enable", havingValue = "true")
public class ThreadPoolAutoConfiguration {

    /**
     * Thread pool executor thread pool executor.
     *
     * @param properties the properties
     * @return the thread pool executor
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolExecutorProperties properties) {
        return new ThreadPoolExecutor(properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }
}
