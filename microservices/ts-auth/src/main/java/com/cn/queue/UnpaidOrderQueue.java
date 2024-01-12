
package com.cn.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 订单自动取消处理类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UnpaidOrderQueue {

    private final RDelayedQueue<String> delayedQueue;


    /**
     * 追加一个订单至队列中.
     *
     * @param orderId the order id
     */
    public void add(final String orderId) {
        delayedQueue.offer(orderId, 290, TimeUnit.SECONDS);
    }
}
