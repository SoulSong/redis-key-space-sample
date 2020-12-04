package com.shf.sample;

import com.shf.sample.listener.RedisKeyExpiredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author songhaifeng
 */
@SpringBootApplication
@Slf4j
public class RedisKeySpaceSampleApplication implements CommandLineRunner {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisKeySpaceSampleApplication.class, args);
    }

    @EventListener(value = RedisKeyExpiredEvent.class)
    public void handleRedisKeyExpire(RedisKeyExpiredEvent expiredEvent) {
        log.info("Key[{}] had been expired.", expiredEvent.getKey());
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 10; i++) {
            log.info("set key:{} with expire time:2s", "foo_" + i);
            redisTemplate.opsForValue().set("foo_" + i, "value_" + i, 2, TimeUnit.SECONDS);
            Thread.sleep(3 * 1000);
        }
    }
}
