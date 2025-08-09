package com.cineflex.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    @Bean
    RedisTemplate<String, Integer> viewHistoryTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> redis = new RedisTemplate<>();
        redis.setConnectionFactory(connectionFactory);
        redis.setKeySerializer(new StringRedisSerializer());
        redis.setValueSerializer(new GenericJackson2JsonRedisSerializer()); 

        return redis;
    }

    @Primary
    @Bean
    RedisTemplate<String, String> forgotPasswordTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redis = new RedisTemplate<>();
        redis.setConnectionFactory(connectionFactory);
        redis.setKeySerializer(new StringRedisSerializer());
        redis.setValueSerializer(new GenericJackson2JsonRedisSerializer()); 


        return redis;
    }
}
