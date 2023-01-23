package com.example.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserJoinDto {
    private String email;
    private String password;
    private String nickname;
}
