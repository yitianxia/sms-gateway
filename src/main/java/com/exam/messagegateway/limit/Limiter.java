package com.exam.messagegateway.limit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class Limiter {

    public static final String telKeyFormat = "tellimit_%d%s";

    private static final  int limit = 10;

    private static final int intervalTime = 1000;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean isTelNumValid(String telNum) {
        LocalDateTime time = LocalDateTime.now();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String key = String.format(telKeyFormat, time.getSecond(),telNum);
        if (redisTemplate.hasKey(key)) {
            return false;
        }
        valueOperations.set(key,1,2, TimeUnit.SECONDS);
        return true;
    }

    public boolean enableTransMsg() {
        Long currentTime = new Date().getTime();
        if(redisTemplate.hasKey("limit")) {
            Integer count = redisTemplate.opsForZSet().rangeByScore("limit", currentTime -  intervalTime, currentTime).size();
            if (count != null && count > limit) {
                return false;
            }
        }
        redisTemplate.opsForZSet().add("limit", UUID.randomUUID().toString(),currentTime);
        return true;
    }

}
