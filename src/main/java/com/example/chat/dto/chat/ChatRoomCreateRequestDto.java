package com.example.chat.dto.chat;

import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomCreateRequestDto {
    private String chatRoomName;
    @Nullable
    private String chatRoomImgUrl;
    List<Long> participantIdList; // 생성자를 제외한 참가자들의 id값 리스트
}
