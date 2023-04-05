package com.example.chat.dto.chat;

import lombok.*;

@Builder
@Data
@ToString
public class ChatMessageResponseDto<T> {
    private String resultCode;
    private String username;
    private T result;

    public static <T> ChatMessageResponseDto<T> success(T result, String username) {
        return ChatMessageResponseDto.<T>builder()
                .resultCode("SUCCESS")
                .username(username)
                .result(result)
                .build();
    }

    public static <T> ChatMessageResponseDto<T> error(T result, String username) {
        return ChatMessageResponseDto.<T>builder()
                .resultCode("ERROR")
                .username(username)
                .result(result)
                .build();
    }

}
