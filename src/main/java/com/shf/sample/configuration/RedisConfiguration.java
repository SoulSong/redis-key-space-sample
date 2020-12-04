package com.shf.sample.configuration;

import com.shf.sample.listener.KeyExpiredNotificationMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.Executors;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/12/4 2:27
 */
@Configuration
public class RedisConfiguration {

    private static final Topic KEYEVENT_EXPIRED_TOPIC = new PatternTopic("__keyevent@*__:expired");

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setConnectionFactory(factory);
        template.setKeySerializer(redisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(redisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public KeyExpiredNotificationMessageListener keyExpiredNotificationMessageListener() {
        return new KeyExpiredNotificationMessageListener();
    }

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(keyExpiredNotificationMessageListener());
    }

    @Bean
    RedisMessageListenerContainer redisContainer(LettuceConnectionFactory lettuceConnectionFactory) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(messageListener(), KEYEVENT_EXPIRED_TOPIC);
        container.setTaskExecutor(Executors.newFixedThreadPool(4));
        return container;
    }
}