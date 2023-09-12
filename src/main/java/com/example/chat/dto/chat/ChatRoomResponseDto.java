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
    private Long chatRoomId;
    private String chatRoomName;
    private int totalNum;
    private String chatRoomImgUrl;
    @Nullable
    private LocalDateTime lastMessageDateTime;
    @Nullable
    private String lastMessageContent;

    public static Page<ChatRoomResponseDto> pageList(Page<ChatRoom> chatRooms) {
        return chatRooms.map(chatRoom -> ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getRoomName())
                .totalNum(chatRoom.getTotalNum())
                .build()
        );
    }

    public static ChatRoomResponseDto ofChatRoomWithChat(ChatRoom chatRoom, Chat chat) {

        return ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getRoomName())
                .totalNum(chatRoom.getTotalNum())
                .lastMessageDateTime(chat.getCreatedDate())
                .lastMessageContent(chat.getMessage())
                .build();
    }
    public static ChatRoomResponseDto ofChatRoom(ChatRoom chatRoom) {

        return ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getRoomName())
                .totalNum(chatRoom.getTotalNum())
                .build();
    }
}
