package com.shf.sample.listener;

/**
 * description :
 *
 * @author songhaifeng
 * @date 2020/12/4 18:10
 */
public class RedisKeyExpiredEvent {

    private String key;
    private String channel;

    public RedisKeyExpiredEvent(String key, String channel) {
        this.key = key;
        this.channel = channel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
