package com.exam.messagegateway.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterInfo {
    @NotBlank
    @Length(min = 3, max = 32)
    @Pattern(regexp = "^[a-z0-9A-Z]+$", message = "invalid username")
    private String userName;

    @NotBlank
    @Length(min = 8, max = 64)
    @Pattern(regexp = "^[a-z0-9A-Z]+$", message = "invalid password")
    private String password;
}
