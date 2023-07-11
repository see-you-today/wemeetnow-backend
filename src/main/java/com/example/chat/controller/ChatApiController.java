package com.example.chat.controller;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.domain.ChatParticipant;
import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.result.Response;
import com.example.chat.dto.TokenDto;
import com.example.chat.dto.chat.ChatDto;
import com.example.chat.dto.chat.ChatInviteRequestDto;
import com.example.chat.dto.chat.ChatRequestDto;
import com.example.chat.dto.chat.ChatRoomCreateRequestDto;
import com.example.chat.exception.ErrorCode;
import com.example.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatApiController {
    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations template;

    /**
     * 사용자 초대
     * authorizationHeader 필요
     * */
    @PostMapping("/invite")
    public Response<List<ChatParticipant>> inviteUser(@RequestBody ChatInviteRequestDto requestDto) {
        log.info("ChatInviteRequestDto: ", requestDto);
        List<ChatParticipant> chatParticipants = chatRoomService.inviteMember(requestDto);
        return Response.success(chatParticipants);
    }

    /**
     * 메시지 보내기
     * */
    @MessageMapping("/send-message")
    public void sendMessage( @Payload ChatRequestDto requestDto) {
        // "/sub/chat/{channelId}" 채널을 구독 중인 클라이언트에게 메시지를 전송
        log.info("requestDto = " + requestDto);
        template.convertAndSend("/sub/chat/room/" + requestDto.getRoomId(), requestDto);
    }

    @MessageMapping("/receive-to")
    public void receiveTo(ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
        log.info("receiveTo() / chatDto = " + chatDto);
        Long senderId = chatDto.getSenderId();
        Long receiverId = chatDto.getReceiverId();
        template.convertAndSend("/send/to/" + receiverId, chatDto);
    }
    /**
     * 채팅방 입장
     * */
    @MessageMapping("/enter")
    public void enter(@Payload ChatRequestDto requestDto, SimpMessageHeaderAccessor accessor) {
        log.info("requestDto = " + requestDto);
        accessor.getSessionAttributes().put("username", requestDto.getUserName());
        template.convertAndSend("/sub/chat/room/" + requestDto.getRoomId(), requestDto);

    }
}