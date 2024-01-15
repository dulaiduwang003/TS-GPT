/**
 * @author 明明不是下雨天
 */
package com.cn.configuration;

import com.cn.constant.OrdersConstant;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis延迟队列配置
 *
 * @author bdth @github dulaiduwang003
 * @version 1.0
 */
@Configuration
public class RedisDelayQueueConfig {

    /**
     * 设置订单堵塞队列
     *
     * @param redissonClient the redisson client
     * @return the r blocking deque
     */
    @Bean
    public RBlockingDeque<String> blockingDeque(RedissonClient redissonClient) {

        return redissonClient.getBlockingDeque(OrdersConstant.ORDERS_QUEUE);
    }

    /**
     * 延迟队列配置
     *
     * @param redissonClient the redisson client
     * @return the r delayed queue
     */
    @Bean
    public RDelayedQueue<String> delayedQueue(RedissonClient redissonClient) {
        return redissonClient.getDelayedQueue(blockingDeque(redissonClient));
    }
}
