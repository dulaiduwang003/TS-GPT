package com.cn.app.superbot.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * The type Redis lock helper.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Component
@RequiredArgsConstructor
@Slf4j
public final class RedisLockHelper {


    /**
     * The Redis template.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    private final RedisTemplate<String, String> redisTemplate;


    /**
     * Lock boolean.
     *
     * @param targetKey the target key
     * @param timeStamp the time stamp
     * @return the boolean
     */
    public boolean lock(String targetKey, String timeStamp) {

        if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(targetKey, timeStamp))) {

            return Boolean.TRUE;
        }

        String currentLock = redisTemplate.opsForValue().get(targetKey);
        if (StringUtils.notEmpty(currentLock)) {
            assert currentLock != null;
            final long val = Long.parseLong(currentLock);

            if (val > System.currentTimeMillis()) {

                String preLock = redisTemplate.opsForValue().getAndSet(targetKey, timeStamp);

                return StringUtils.notEmpty(preLock) && Objects.equals(preLock, currentLock);
            }

        }
        return false;

    }


    /**
     * Unlock.
     *
     * @param targetKey the target key
     * @param timeStamp the time stamp
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public void unlock(String targetKey, String timeStamp) {

        try {
            String currentValue = redisTemplate.opsForValue().get(targetKey);
            if (StringUtils.notEmpty(currentValue) && Objects.equals(currentValue, timeStamp)) {
                redisTemplate.opsForValue().getOperations().delete(targetKey);
            }
        } catch (Exception e) {
            log.error("error", targetKey, e.getMessage());
        }

    }

}
