package com.exam.messagegateway.service.impl;

import com.exam.messagegateway.dto.ResponseInfo;
import com.exam.messagegateway.service.MessageService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author yin guojian
 * @Date 2022-03-19
 */
@Service
public class MessageServiceImpl implements MessageService {
    public ResponseInfo send(String str) {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = "http://mock-sms-server:8080/v2/emp/templateSms/sendSms";
        HttpPost post = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(str, Charset.forName("UTF-8"));
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        //设置请求参数
        post.setEntity(stringEntity);
        try {
            HttpResponse execute = client.execute(post);
            HttpEntity entiy = execute.getEntity();
            String res = EntityUtils.toString(entiy);
//            JSONObject js = JSONObject.parseObject(res);
            if (res.indexOf("success")!=0){
                return new ResponseInfo(200,"success");
            }else {
                return new ResponseInfo(500,"内部服务器错误");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseInfo(500,"内部服务器错误");
    }

}
