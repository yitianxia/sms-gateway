package com.exam.messagegateway.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserLoginInfo {
    @NotBlank
    @Length(min = 3, max = 32, message = "invalid username length")
    @Pattern(regexp = "^[a-z0-9A-Z]+$", message = "invalid username")
    private String userName;

    @NotBlank
    @Length(min = 8, max = 64, message = "invalid password length")
    @Pattern(regexp = "^[a-z0-9A-Z]+$", message = "invalid password")
    private String password;
}
