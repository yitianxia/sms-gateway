package com.exam.messagegateway.controller;

import com.exam.messagegateway.dto.*;
import com.exam.messagegateway.service.SessionService;
import com.exam.messagegateway.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author yin guojian
 * @Date 2022-03-18
 */
@Slf4j
@RestController
@RequestMapping("/auth/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @PostMapping(value = "/logout")
    public ResponseInfo logout(@RequestBody @Validated UserLogOutInfo info) {
        try {
            if (info == null) {
                return new ResponseInfo(400, "请求参数错误");
            }
            if (!userService.getUserPwdByName(info.getUserName()).isPresent()) {
                return new ResponseInfo(400, "请求参数错误");
            }
            if (sessionService.getSessionIdByUser(info.getUserName()).isPresent()) {
                if (!sessionService.getSessionIdByUser(info.getUserName()).get().equals(info.getSessionId())){
                    return new ResponseInfo(400, "请求参数错误");
                }
            }else {
                return new ResponseInfo(400, "请求参数错误");
            }

            if (!sessionService.isSessionIdExist(info.getSessionId())) {
                return new ResponseInfo(400, "请求参数错误");
            }
            sessionService.clearUserSessionId(info.getUserName());
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseInfo(500, "内部服务器错误");
        }

        return ResponseInfo.success();
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseInfo register(@RequestBody @Validated UserRegisterInfo user) {
       try {
           if (user == null) {
               return new ResponseInfo(400, "请求参数错误");
           }
           // 已经注册过了
           if (userService.getUserPwdByName(user.getUserName()).isPresent()) {
               return new ResponseInfo(400, "请求参数错误");
           }
           userService.addUser(user.getUserName(), user.getPassword());
       }catch (Exception e) {
           e.printStackTrace();
           return new ResponseInfo(500, "内部服务器错误");
       }

        return ResponseInfo.success();
    }

    @PostMapping(value = "/login")
    public UserLoginResponse login(@RequestBody @Validated UserLoginInfo user, BindingResult bindingResult) {
        if (userService.getUserPwdByName(user.getUserName()).isPresent()) {
            if (!userService.getUserPwdByName(user.getUserName()).get().equals(user.getPassword())) {
                return UserLoginResponse.failure("输入参数错误");
            }
            String sessionId;
            if (sessionService.getSessionIdByUser(user.getUserName()).isPresent()) {
                sessionId = sessionService.getSessionIdByUser(user.getUserName()).get();
                return UserLoginResponse.success(sessionId);
            }
            sessionId = UUID.randomUUID().toString();
            sessionService.addSessionId(user.getUserName(), sessionId);
            return UserLoginResponse.success(sessionId);
        } else {
            return UserLoginResponse.failure("输入参数错误");
        }
    }
}
