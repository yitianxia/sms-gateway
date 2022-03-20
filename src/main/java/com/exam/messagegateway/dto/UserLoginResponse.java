package com.exam.messagegateway.dto;

import lombok.Data;

@Data
public class UserLoginResponse {
    private int code;
    private String message;
    private String sessionId;

    public UserLoginResponse() {
    }

    public UserLoginResponse(int code, String message, String sessionId) {
        this.code = code;
        this.message = message;
        this.sessionId = sessionId;
    }

    public static UserLoginResponse success(String sessionId) {
        return new UserLoginResponse(200, "success", sessionId);
    }

    public static UserLoginResponse failure(String message) {
        UserLoginResponse response = new UserLoginResponse();
        response.setCode(400);
        response.setMessage(message);
        return response;
    }
}
