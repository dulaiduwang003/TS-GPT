package com.cn.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Redis参数配置
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Configuration
@SuppressWarnings("all")
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig {

    /**
     * Jackson 2 json redis serializer jackson 2 json redis serializer.
     *
     * @return the jackson 2 json redis serializer
     */
    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(timeModule());
        serializer.setObjectMapper(om);
        return serializer;
    }

    /**
     * String redis serializer string redis serializer.
     *
     * @return the string redis serializer
     */
    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }


    /**
     * Redis template redis template.
     *
     * @param lettuceConnectionFactory    the lettuce connection factory
     * @param jackson2JsonRedisSerializer the jackson 2 json redis serializer
     * @param stringRedisSerializer       the string redis serializer
     * @return the redis template
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory,
                                                       Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer,
                                                       StringRedisSerializer stringRedisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }


    /**
     * Redis cache configuration redis cache configuration.
     *
     * @param jackson2JsonRedisSerializer the jackson 2 json redis serializer
     * @param redisSerializer             the redis serializer
     * @return the redis cache configuration
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer,
                                                           RedisSerializer<String> redisSerializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(3600 * 24 * 10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();
    }

    /**
     * Cache manager cache manager.
     *
     * @param factory                     the factory
     * @param redisSerializer             the redis serializer
     * @param jackson2JsonRedisSerializer the jackson 2 json redis serializer
     * @param redisCacheConfiguration     the redis cache configuration
     * @return the cache manager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory,
                                     RedisSerializer<String> redisSerializer,
                                     Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer,
                                     RedisCacheConfiguration redisCacheConfiguration) {
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
        return cacheManager;
    }

    /**
     * Time module java time module.
     *
     * @return the java time module
     */
    @Bean
    public JavaTimeModule timeModule() {
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return timeModule;
    }


}
