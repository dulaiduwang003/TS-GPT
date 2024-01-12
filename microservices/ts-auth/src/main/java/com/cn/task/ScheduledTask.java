package com.cn.task;

import cn.dev33.satoken.same.SaSameUtil;
import com.cn.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.cn.constant.StatisticsConstant.*;

/**
 * auth定时器
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ScheduledTask {

    private final RedisUtils redisUtils;


    /**
     * 刷新子服务之间调用令牌
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void refreshToken() {
        SaSameUtil.refreshToken();
    }


    /**
     * 刷新今日数据
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void refreshSiftData() {
        redisUtils.delKey(NEW_REVENUE);
        redisUtils.delKey(NEW_TRADE);
        redisUtils.delKey(NEW_USERS);
        redisUtils.delKey(NEW_VISITS);
        redisUtils.delKey(TOTAL_VISITS);
    }
}
