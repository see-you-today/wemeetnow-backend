package com.example.chat.dto.friend;

import com.example.chat.domain.Friend;
import com.example.chat.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendResponseDto {
    private Long userId;
    private String userName;
    private String userImgUrl;

    public static FriendResponseDto toDto(User user) {

        return FriendResponseDto.builder()
                .userId(user.getId())
                .userName(user.getUsername())
                .userImgUrl(user.getImgUrl())
                .build();
    }
}
