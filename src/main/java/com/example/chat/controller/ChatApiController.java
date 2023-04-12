package com.example.chat.controller;

import com.example.chat.domain.ChatParticipant;
import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.result.Response;
import com.example.chat.dto.chat.ChatInviteRequestDto;
import com.example.chat.dto.chat.ChatRoomRequestDto;
import com.example.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatApiController {
    private final ChatService chatService;

    /**
     * 채팅방 생성
     * */
    @PostMapping("/create-chatroom")
    public Response<ChatRoom> createChatRoom(@RequestBody ChatRoomRequestDto requestDto) {
        System.out.println("ChatRoomRequestDto = " + requestDto);
        ChatRoom chatRoom = chatService.createChatRoom(requestDto);
        return Response.success(chatRoom);
    }

    /**
     * 사용자 초대
     * */
    @PostMapping("/invite")
    public Response<List<ChatParticipant>> inviteUser(@RequestBody ChatInviteRequestDto requestDto) {
        log.info("ChatInviteRequestDto: ", requestDto);
        List<ChatParticipant> chatParticipants = chatService.inviteMember(requestDto);
        return Response.success(chatParticipants);
    }
}
