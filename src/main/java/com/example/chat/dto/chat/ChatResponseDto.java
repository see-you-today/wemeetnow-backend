package com.example.chat.dto.chat;

import com.example.chat.domain.Chat;
import com.example.chat.domain.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponseDto {
    private Long id;
    private ChatType chatType;
    private boolean iamSender;
    private String senderName;
    private String senderImgUrl;
    private LocalDateTime sendDateTime;
    private String content;
    private int notReadCount;

    /**
     * User의 맴버를 touch함
     * 채팅방에 초대한 메시지의 경우엔 invited
     * */
    public static ChatResponseDto createWithChat(Chat chat, boolean iamSender) {
        return ChatResponseDto.builder()
                .id(chat.getId())
                .chatType(chat.getChatType())
                .iamSender(iamSender)
                .senderName(chat.getUser().getUsername())
                .senderImgUrl(chat.getUser().getImgUrl())
                .sendDateTime(chat.getCreatedDate())
                .content(chat.getMessage())
                .notReadCount(chat.getNotReadCount())
                .build();
    }
}
