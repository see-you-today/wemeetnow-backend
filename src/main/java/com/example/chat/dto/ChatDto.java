package com.example.chat.dto;

import lombok.Data;

@Data
public class ChatDto {
    private Long channelId;
    private Long writerId;
    private String chat;
}
