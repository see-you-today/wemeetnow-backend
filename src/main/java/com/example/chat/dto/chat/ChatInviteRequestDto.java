package com.example.chat.dto.chat;

import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatInviteRequestDto {
    private List<Long> userIdList;
}
