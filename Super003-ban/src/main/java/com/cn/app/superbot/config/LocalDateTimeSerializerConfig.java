package com.cn.app.superbot.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Local date time serializer config.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Configuration
public class LocalDateTimeSerializerConfig {


    /**
     * Local date time deserializer local date time serializer.
     *
     * @return the local date time serializer
     * @author bdth
     * @email 2074055628 @qq.om
     */
    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Jackson 2 object mapper builder customizer jackson 2 object mapper builder customizer.
     *
     * @return the jackson 2 object mapper builder customizer
     * @author bdth
     * @email 2074055628 @qq.om
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }

}
