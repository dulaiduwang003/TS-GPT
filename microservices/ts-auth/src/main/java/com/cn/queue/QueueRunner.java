
package com.cn.queue;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.constant.OrdersStatusConstant;
import com.cn.entity.TsOrders;
import com.cn.mapper.TsOrdersMapper;
import com.cn.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

import static com.cn.constant.OrdersConstant.ORDER_PAY_STATUS;

/**
 * 订单失效处理器
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("all")
public class QueueRunner implements CommandLineRunner {

    private final RBlockingDeque<String> blockingFairQueue;

    private final TsOrdersMapper tsOrdersMapper;

    private final ThreadPoolExecutor threadPoolExecutor;

    private final RedisUtils redisUtils;

    @Override
    public void run(String... args) {
        threadPoolExecutor.execute(() -> {
            while (true) {
                String orderNo;
                try {
                    //获取订单号
                    orderNo = blockingFairQueue.take();
                } catch (Exception e) {
                    continue;
                }
                final TsOrders superOrder = tsOrdersMapper.selectOne(new QueryWrapper<TsOrders>()
                        .lambda()
                        .eq(TsOrders::getOrdersId, orderNo)
                        .eq(TsOrders::getStatus, OrdersStatusConstant.SUCCEED)
                        .select(TsOrders::getOrdersId)
                );
                if (superOrder == null) {
                    tsOrdersMapper.updateById(new TsOrders()
                            .setOrdersId(orderNo)
                            .setStatus(OrdersStatusConstant.CANCEL)
                    );
                    redisUtils.delKey(ORDER_PAY_STATUS + orderNo);
                }
            }
        });
    }
}
