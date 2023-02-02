package com.example.chat.dto;

import com.example.chat.config.jwt.JwtUtilEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.chat.config.jwt.JwtUtilEnums.*;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    public static TokenDto of(String accessToken, String refreshToken) {
        return TokenDto.builder()
                .grantType(TOKEN_PREFIX.getValue())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
