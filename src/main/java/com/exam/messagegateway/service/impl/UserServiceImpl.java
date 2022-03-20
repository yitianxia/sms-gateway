package com.exam.messagegateway.service.impl;

import com.exam.messagegateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    public Optional<String> getUserPwdByName(String userName){
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        String pwd = (String) valueOperations.get(userName);
        if (pwd != null) {
            return Optional.of(pwd);
        }
        return Optional.empty();
    }
    public void addUser(String userName, String password){
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(userName, password);
    }
}
