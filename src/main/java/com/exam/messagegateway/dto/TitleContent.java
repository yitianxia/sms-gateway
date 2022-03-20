package com.exam.messagegateway.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class TitleContent {
    @NotBlank
    @Length(min = 1, max = 64, message = "invalid username length")
    private String title;

    @NotBlank
    private String content;
}
