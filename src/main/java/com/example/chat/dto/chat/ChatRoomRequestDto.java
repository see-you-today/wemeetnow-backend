package com.example.chat.dto.chat;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomRequestDto {
    private String roomName;
    private int totalNum;
}
