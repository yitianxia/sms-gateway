package com.exam.messagegateway.service;

import com.exam.messagegateway.dto.ResponseInfo;

/**
 * @Author yin guojian
 * @Date 2022-03-19
 */
public interface MessageService {

    public ResponseInfo send(String str);

}
