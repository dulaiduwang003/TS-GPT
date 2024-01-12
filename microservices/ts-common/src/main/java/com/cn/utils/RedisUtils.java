package com.cn.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * redis工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@SuppressWarnings("all")
@RequiredArgsConstructor
public final class RedisUtils {

    /**
     * The Redis template.
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Gets redis template.
     *
     * @return the redis template
     */
    public RedisTemplate getRedisTemplate() {
        return this.redisTemplate;
    }


    /**
     * Expire boolean.
     *
     * @param key     the key
     * @param timeout the timeout
     * @return the boolean
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * Gets expire.
     *
     * @param key the key
     * @return the expire
     */
    public Long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }


    /**
     * Expire boolean.
     *
     * @param key     the key
     * @param timeout the timeout
     * @param unit    the unit
     * @return the boolean
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        Boolean ret = redisTemplate.expire(key, timeout, unit);
        return ret != null && ret;
    }

    /**
     * Has key boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean hasKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * Increment long.
     *
     * @param key   the key
     * @param delta the delta
     * @return the long
     */
    public long increment(final String key, final long delta) {

        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * Increment double.
     *
     * @param key   the key
     * @param delta the delta
     * @return the double
     */
    public double increment(final String key, final double delta) {

        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * Del key boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean delKey(final String key) {
        Boolean ret = redisTemplate.delete(key);
        return ret != null && ret;
    }


    /**
     * Del keys long.
     *
     * @param keys the keys
     * @return the long
     */
    public long delKeys(final Collection<String> keys) {
        Long ret = redisTemplate.delete(keys);
        return ret == null ? 0 : ret;
    }


    /**
     * Sets value.
     *
     * @param key   the key
     * @param value the value
     */
    public void setValue(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * Sets value timeout.
     *
     * @param key     the key
     * @param value   the value
     * @param timeout the timeout
     */
    public void setValueTimeout(final String key, final Object value, final long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }


    /**
     * Gets value.
     *
     * @param key the key
     * @return the value
     */
    public Object getValue(final String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * Has hash key boolean.
     *
     * @param key  the key
     * @param hkey the hkey
     * @return the boolean
     */
    public boolean hasHashKey(final String key, String hkey) {
        Boolean ret = redisTemplate.opsForHash().hasKey(key, hkey);
        return ret != null && ret;
    }


    /**
     * Hash put.
     *
     * @param key   the key
     * @param hKey  the h key
     * @param value the value
     */
    public void hashPut(final String key, final String hKey, final Object value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }


    /**
     * Hash put all.
     *
     * @param key    the key
     * @param values the values
     */
    public void hashPutAll(final String key, final Map<String, Object> values) {
        redisTemplate.opsForHash().putAll(key, values);
    }


    /**
     * Hash get object.
     *
     * @param key  the key
     * @param hKey the h key
     * @return the object
     */
    public Object hashGet(final String key, final String hKey) {
        return redisTemplate.opsForHash().get(key, hKey);
    }


    /**
     * Hash get all map.
     *
     * @param key the key
     * @return the map
     */
    public Map<Object, Object> hashGetAll(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * Hash multi get list.
     *
     * @param key   the key
     * @param hKeys the h keys
     * @return the list
     */
    public List<Object> hashMultiGet(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * Hash exists boolean.
     *
     * @param key     the key
     * @param hashKey the hash key
     * @return the boolean
     */
    public boolean hashExists(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }


    /**
     * Hash delete keys long.
     *
     * @param key   the key
     * @param hKeys the h keys
     * @return the long
     */
    public long hashDeleteKeys(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().delete(key, hKeys);
    }

    /**
     * Hash delete long.
     *
     * @param key     the key
     * @param hashKey the hash key
     * @return the long
     */
    public Long hashDelete(final String key, final Object... hashKey) {
        return redisTemplate.opsForHash().delete(key, hashKey);
    }


    /**
     * Sets set.
     *
     * @param key    the key
     * @param values the values
     * @return the set
     */
    public long setSet(final String key, final Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        return count == null ? 0 : count;
    }


    /**
     * Sets del.
     *
     * @param key    the key
     * @param values the values
     * @return the del
     */
    public long setDel(final String key, final Object... values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count == null ? 0 : count;
    }


    /**
     * Gets set all.
     *
     * @param key the key
     * @return the set all
     */
    public Set<Object> getSetAll(final String key) {
        return redisTemplate.opsForSet().members(key);
    }


    /**
     * Zset set all long.
     *
     * @param key    the key
     * @param values the values
     * @return the long
     */
    public long zsetSetAll(final String key, final Set<ZSetOperations.TypedTuple<Object>> values) {
        Long count = redisTemplate.opsForZSet().add(key, values);
        return count == null ? 0 : count;
    }

    /**
     * Zset set get source double.
     *
     * @param key   the key
     * @param value the value
     * @return the double
     */
    public Double zsetSetGetSource(final String key, final Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * Zset increment score double.
     *
     * @param key       the key
     * @param value     the value
     * @param increment the increment
     * @return the double
     */
    public Double zsetIncrementScore(final String key, final Object value, final Double increment) {
        return redisTemplate.opsForZSet().incrementScore(key, value, increment);
    }

    /**
     * Zset set boolean.
     *
     * @param key    the key
     * @param values the values
     * @param source the source
     * @return the boolean
     */
    public Boolean zsetSet(final String key, final Object values, final Double source) {
        final Boolean add = redisTemplate.opsForZSet().add(key, values, source);
        return add;
    }


    /**
     * Zset reverse range with scores set.
     *
     * @param key   the key
     * @param start the start
     * @param end   the end
     * @return the set
     */
    public Set<ZSetOperations.TypedTuple<Object>> zsetReverseRangeWithScores(final String key, final Long start, final Long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }


    /**
     * Zset reverse range set.
     *
     * @param key   the key
     * @param start the start
     * @param end   the end
     * @return the set
     */
    public Set<Object> zsetReverseRange(final String key, final Long start, final Long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }


    /**
     * Self increase long.
     *
     * @param key the key
     * @return the long
     */
    public long selfIncrease(final String key) {
        return redisTemplate.execute(new SessionCallback<Long>() {
            @Override
            public Long execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                Long count = operations.opsForValue().increment(key);
                operations.exec();
                return count;
            }
        });
    }

    /**
     * Self increase source double.
     *
     * @param key   the key
     * @param value the value
     * @return the double
     */
    public Double selfIncreaseSource(final String key, final Object value) {
        return redisTemplate.execute(new SessionCallback<Double>() {
            @Override
            public Double execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                Double count = operations.opsForZSet().incrementScore(key, value, 1);
                operations.exec();
                return count;
            }
        });
    }

    /**
     * Zset del all long.
     *
     * @param key    the key
     * @param values the values
     * @return the long
     */
    public long zsetDelAll(final String key, final Set<ZSetOperations.TypedTuple<Object>> values) {
        Long count = redisTemplate.opsForZSet().remove(key, values);
        return count == null ? 0 : count;
    }

    /**
     * Zset del long.
     *
     * @param key    the key
     * @param values the values
     * @return the long
     */
    public long zsetDel(final String key, Object values) {
        Long count = redisTemplate.opsForZSet().remove(key, values);
        return count == null ? 0 : count;
    }

    /**
     * List push long.
     *
     * @param key   the key
     * @param value the value
     * @return the long
     */
    public long listPush(final String key, final Object value) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count == null ? 0 : count;
    }

    /**
     * Does it exist boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public Boolean doesItExist(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * List push all long.
     *
     * @param key    the key
     * @param values the values
     * @return the long
     */
    public long listPushAll(final String key, final Collection<Object> values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    /**
     * List push all long.
     *
     * @param key    the key
     * @param values the values
     * @return the long
     */
    public long listPushAll(final String key, final Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }


    /**
     * List get list.
     *
     * @param key   the key
     * @param start the start
     * @param end   the end
     * @return the list
     */
    public List<Object> listGet(final String key, final int start, final int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * Key size long.
     *
     * @param key the key
     * @return the long
     */
    public Long keySize(final String key) {
        return redisTemplate.opsForList().size(key);
    }
}
