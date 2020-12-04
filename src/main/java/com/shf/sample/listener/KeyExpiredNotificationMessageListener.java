package com.shf.sample.listener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/12/4 2:29
 */
public class KeyExpiredNotificationMessageListener implements MessageListener, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = applicationContext.getBean("redisTemplate", RedisTemplate.class).getKeySerializer().deserialize(message.getBody()).toString();
        applicationContext.publishEvent(new RedisKeyExpiredEvent(key, new String(message.getChannel())));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}