package com.exam.messagegateway.service;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserService {
    Optional<String> getUserPwdByName(String userName);
    void addUser(String userName, String password);

}
