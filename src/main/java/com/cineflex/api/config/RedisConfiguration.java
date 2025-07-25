package com.cineflex.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
    @Bean
    RedisTemplate<String, Integer> viewHistoryTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> redis = new RedisTemplate<>();
        redis.setConnectionFactory(connectionFactory);

        return redis;
    }
}
