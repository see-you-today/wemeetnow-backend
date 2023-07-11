package com.example.chat.dto.chat;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomCreateRequestDto {
    private String chatRoomName;
    List<Long> participantIdList; // 생성자를 제외한 참가자들의 id값 리스트
}
