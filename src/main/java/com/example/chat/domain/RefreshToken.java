package com.example.chat.domain;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@AllArgsConstructor
//@NoArgsConstructor
@RedisHash("refreshToken")
@Builder
//@Data
//@Entity
public class RefreshToken {
    @Id
    private String id;

    private String refreshToken;

    @TimeToLive
    private Long expiration;

    public static RefreshToken createRefreshToken(String username, String refreshToken, Long remainingMilliSeconds) {
        return RefreshToken.builder()
                .id(username)
                .refreshToken(refreshToken)
                .expiration(remainingMilliSeconds / 1000)
                .build();
    }
}
