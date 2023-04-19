package com.example.chat.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private Long roomId;
    private Long senderId;
    private Long receiverId;
    private String content1;
    private String content2;
}
