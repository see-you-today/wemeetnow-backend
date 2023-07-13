package com.example.chat.dto;

import com.example.chat.domain.Chat;
import com.example.chat.dto.chat.ChatResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatListResponseDto {
    private List<ChatResponseDto> chatDtoList;

    public static ChatListResponseDto of(List<ChatResponseDto> chatResponseDtoList)  {
        return new ChatListResponseDto(chatResponseDtoList);
    }
}
