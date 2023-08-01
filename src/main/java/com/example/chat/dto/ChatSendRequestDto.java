package com.example.chat.dto;

import com.example.chat.domain.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSendRequestDto {
    /**
     * 채팅 보내기 기능에서 FE로 부터 전달받는 DTO
     * header 정보로 누가 보냈는지 알 수 있기 때문에 보낸이 정보는 없어도 됨
     * */
    private Long chatRoomId;
    private String content;
    private ChatType chatType;
    @Nullable
    private String invitedUserName;
}
