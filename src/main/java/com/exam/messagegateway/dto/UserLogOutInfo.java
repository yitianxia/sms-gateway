package com.exam.messagegateway.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class UserLogOutInfo {
    @NotBlank
    @Length(min = 3)
    private String userName;

    @NotBlank
    private String sessionId;
}
