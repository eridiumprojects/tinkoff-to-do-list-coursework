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
    @Bean
    RedissonClient redissonClient(
            ObjectMapper objectMapper,
            @Value("${spring.data.redis.url}") String url
            ){
        Config config = getCommonConfig(objectMapper);
        config.useSingleServer().setAddress((url));

        return Redisson.create(config);
    }

    private Config getCommonConfig(ObjectMapper objectMapper) {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec(objectMapper));
        return config;
    }
}