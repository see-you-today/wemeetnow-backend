package com.example.chat.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RefreshTokenDto {
    @NotNull
    private String refreshToken;
}
