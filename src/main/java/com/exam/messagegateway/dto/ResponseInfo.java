package com.exam.messagegateway.dto;

import lombok.Data;

@Data
public class ResponseInfo {
    private int code;
    private String message;

    public ResponseInfo() {
    }

    public ResponseInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseInfo success(){
        return new ResponseInfo(200, "success");
    }
}
