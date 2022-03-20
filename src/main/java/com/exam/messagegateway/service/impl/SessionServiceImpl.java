package com.exam.messagegateway.service.impl;

import com.exam.messagegateway.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {
    private static final String keyFormat = "%s_sessionId";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addSessionId(String userName, String sessionId) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(String.format(keyFormat, userName), sessionId);
        valueOperations.set(sessionId, 1);
    }

    public Optional<String> getSessionIdByUser(String userName) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String sessionId = (String) valueOperations.get(String.format(keyFormat, userName));
        if (sessionId == null) {
            return Optional.empty();
        }
        return Optional.of(sessionId);
    }

    public void clearUserSessionId(String userName){
        if (getSessionIdByUser(userName).isPresent()) {
            redisTemplate.delete(getSessionIdByUser(userName).get());
        }
        redisTemplate.delete(String.format(keyFormat, userName));
    }

    public boolean isUserLogin(String userName) {
        return getSessionIdByUser(userName).isPresent();
    }

    public boolean isSessionIdExist(String sessionId){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get(sessionId) != null) {
            return true;
        }
        return false;
    }
}
