package com.example.chat.dto.chat;

import com.example.chat.domain.Chat;
import com.example.chat.domain.ChatRoom;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Builder
@Data
public class ChatRoomResponseDto {
    private Long roomId;
    private String roomName;
    private int totalNum;
    private String chatRoomImg;
    @Nullable
    private LocalDateTime lastMessageDateTime;
    @Nullable
    private String lastMessageContent;

    public static Page<ChatRoomResponseDto> pageList(Page<ChatRoom> chatRooms) {
        return chatRooms.map(chatRoom -> ChatRoomResponseDto.builder()
                .roomId(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .totalNum(chatRoom.getTotalNum())
                .build()
        );
    }

    public static ChatRoomResponseDto fromEntityWithChat(ChatRoom chatRoom, Chat chat) {

        return ChatRoomResponseDto.builder()
                .roomId(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .totalNum(chatRoom.getTotalNum())
                .lastMessageDateTime(chat.getCreatedDate())
                .lastMessageContent(chat.getMessage())
                .build();
    }
    public static ChatRoomResponseDto fromEntityNoChat(ChatRoom chatRoom) {

        return ChatRoomResponseDto.builder()
                .roomId(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .totalNum(chatRoom.getTotalNum())
                .build();
    }
}
