package com.example.chat.dto.chat;

import com.example.chat.domain.ChatRoom;
import lombok.*;
import org.springframework.data.domain.Page;

@Builder
@Data
public class ChatRoomResponseDto {
    private Long roomId;
    private String roomName;
    private int totalNum;

    public static Page<ChatRoomResponseDto> pageList(Page<ChatRoom> chatRooms) {
        return chatRooms.map(chatRoom -> ChatRoomResponseDto.builder()
                .roomId(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .totalNum(chatRoom.getTotalNum())
                .build()
        );
    }

    public static ChatRoomResponseDto fromEntity(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .roomId(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .totalNum(chatRoom.getTotalNum())
                .build();
    }
}
