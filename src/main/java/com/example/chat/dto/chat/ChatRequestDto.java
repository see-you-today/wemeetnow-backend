package com.example.chat.dto.chat;

import com.example.chat.domain.enums.ChatType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ChatRequestDto {
    private Long roomId;
    private Long userId;
    private String userName;
    private ChatType chatType;
    private String message;

}
