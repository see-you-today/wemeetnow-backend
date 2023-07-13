package com.example.chat.service;

import com.example.chat.domain.Chat;
import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.User;
import com.example.chat.dto.ChatListResponseDto;
import com.example.chat.dto.ChatSendRequestDto;
import com.example.chat.dto.chat.ChatResponseDto;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void createChat(Long userId, ChatSendRequestDto requestDto) {
        try {
            User findUser =  userService.getUserById(userId);
            ChatRoom chatRoom = chatRoomRepository.findById(requestDto.getChatRoomId()).get();
            Chat newChat = Chat.createChat(findUser, chatRoom, requestDto);
            chatRepository.save(newChat);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("e: ", e.getStackTrace());
        }
    }

    public ChatListResponseDto getListWithChatRoomId(Long loginedUserId, Long chatRoomId) {
        List<ChatResponseDto> chatResponseDtoList =
                chatRepository.findByChatRoomId(chatRoomId)
                        .stream().map(c -> ChatResponseDto.createWithChat(c, c.getUser().getId() == loginedUserId))
                        .collect(Collectors.toList());
        ChatListResponseDto chatListResponseDto = ChatListResponseDto.of(chatResponseDtoList);
        return chatListResponseDto;
    }
}
