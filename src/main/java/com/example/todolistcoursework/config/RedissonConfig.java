package com.example.todolistcoursework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.url}")
    public String url;

    private static String redisUrl(String url) {
        return url;
    }

    @Bean
    RedissonClient redissonClient(
            ObjectMapper objectMapper
            ) {
        Config config = getCommonConfig(objectMapper);
        config.useSingleServer().setAddress(redisUrl(url));

        return Redisson.create(config);
    }

    private Config getCommonConfig(ObjectMapper objectMapper) {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec(objectMapper));
        return config;
    }
}