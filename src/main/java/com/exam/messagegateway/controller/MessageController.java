package com.exam.messagegateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exam.messagegateway.dto.ResponseInfo;
import com.exam.messagegateway.dto.TitleContent;
import com.exam.messagegateway.limit.Limiter;
import com.exam.messagegateway.service.MessageService;
import com.exam.messagegateway.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;


/**
 * @Author yin guojian
 * @Date 2022-03-18
 */
@RestController
@RequestMapping("/directmessage")
public class MessageController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private Limiter limiter;

    @Autowired
    private MessageService mssageService;


    @PostMapping(value = "", produces = "application/json;charset=UTF-8")
    public ResponseInfo directmessage(@RequestParam(value = "tels", required=false) String tels,
                                      @RequestParam(value = "qos",required=false) String qos,
                                      @RequestParam(value = "userName",required=false) String userName,
                                      @RequestParam(value = "sessionId",required=false) String sessionId,
                                      @RequestBody @Validated TitleContent titleContent){
        if (!(sessionService.getSessionIdByUser(userName).isPresent() && sessionId.equals(sessionService.getSessionIdByUser(userName).get()))) {
            return new ResponseInfo(403,"禁止未经授权访问");
        }
        if (tels==null||qos==null||userName==null||sessionId==null) {
            return new ResponseInfo(400,"请求参数错误");
        }
        if (tels.isEmpty()||qos.isEmpty()||userName.isEmpty()||sessionId.isEmpty()) {
            return new ResponseInfo(400,"请求参数错误");
        }

        JSONObject json = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-Mdd HH:mm:ss");
        String formatTime = df.format(System.currentTimeMillis());
        json.put("qos", qos);
        json.put("acceptor_tel", tels);
        JSONObject json2 = new JSONObject();
        json2.put("title", titleContent.getTitle());
        json2.put("content", titleContent.getContent());
        json.put("template_param", json2);
        json.put("timestamp",formatTime);
        String str = JSON.toJSONString(json);
        try {
            while(!limiter.isTelNumValid(tels)) {
            }
            while (!limiter.enableTransMsg()) {
            }
            return mssageService.send(str);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseInfo(500,"内部服务器错误");
        }
    }



}
