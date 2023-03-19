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

    private static String redisUrl(String host, String port) {
        return "redis://%s:%s".formatted(host, port);
    }

    @Bean
    RedissonClient redissonClient(
            ObjectMapper objectMapper,
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") String port
    ) {
        Config config = getCommonConfig(objectMapper);
        config.useSingleServer().setAddress(redisUrl(host, port));

        return Redisson.create(config);
    }

    private Config getCommonConfig(ObjectMapper objectMapper) {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec(objectMapper));
        return config;
    }
}
