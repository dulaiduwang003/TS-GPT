package com.cn.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * redis lock
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public final class RedisLockHelper {

    /**
     * The Redis template.
     */
    private final RedisTemplate<String, String> redisTemplate;
    /**
     * The constant MAX_RETRIES.
     */
    private static final int MAX_RETRIES = 5; // 最大重试次数
    /**
     * The constant LOCK_EXPIRE_TIME.
     */
    private static final long LOCK_EXPIRE_TIME = 5L; // 锁的过期时间（秒）

    /**
     * Lock boolean.
     *
     * @param targetKey the target key
     * @param timeStamp the time stamp
     * @return the boolean
     */
    public boolean lock(String targetKey, String timeStamp) {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(targetKey, timeStamp))) {
                return Boolean.TRUE;
            } else {
                retries++;
                if (retries < MAX_RETRIES) {
                    // 等待一段时间后重试
                    try {
                        Thread.sleep(1000); // 等待1秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 重试次数超过最大值，返回false
                    return false;
                }
            }
        }
        return false; // 如果重试次数超过最大值，仍然返回false
    }

    public void unlock(String targetKey, String timeStamp) {
        try {
            String currentValue = redisTemplate.opsForValue().get(targetKey);
            if (currentValue != null && Objects.equals(currentValue, timeStamp)) {
                redisTemplate.opsForValue().getOperations().delete(targetKey);
            } else {
                log.error("解锁失败 KEY:{}, 时间戳不匹配", targetKey);
            }
        } catch (Exception e) {
            log.error("锁错误 KEY:{},错误信息{}", targetKey, e.getMessage());
        }
    }

}
