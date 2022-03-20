package com.exam.messagegateway.service;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface SessionService {
    void addSessionId(String userName, String sessionId);
    Optional<String> getSessionIdByUser(String userName);
    void clearUserSessionId(String userName);
    boolean isUserLogin(String userName);
    boolean isSessionIdExist(String sessionId);
}
