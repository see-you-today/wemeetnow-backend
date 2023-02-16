package com.example.chat.dto;

import com.example.chat.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinResponseDto {
    private Long userId;
    public static UserJoinResponseDto toDto(User user){
        return new UserJoinResponseDto(user.getId());
    }
}
