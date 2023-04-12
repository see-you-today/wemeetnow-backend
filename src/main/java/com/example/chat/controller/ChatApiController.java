package com.example.chat.controller;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.domain.ChatParticipant;
import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.result.Response;
import com.example.chat.dto.TokenDto;
import com.example.chat.dto.chat.ChatInviteRequestDto;
import com.example.chat.dto.chat.ChatRoomRequestDto;
import com.example.chat.exception.ErrorCode;
import com.example.chat.service.ChatService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatApiController {
    private final ChatService chatService;
    private final SimpMessageSendingOperations template;

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

    /**
     * 사용자가 참여중인 채팅방 전체 목록 조회
     */
    @GetMapping("")
    public Response getChatRoomList(TokenDto tokenDto) {
        String accessToken = tokenDto.getAccessToken();
        if (JwtUtil.isExpired(accessToken)) {
            return Response.error(ErrorCode.INVALID_TOKEN);
        }
        Long userId = JwtUtil.getId(accessToken);
        System.out.println("userId from accessToken = " + userId);
        List<ChatRoom> chatRooms = chatService.findAllChatRoomByUserId(userId);
        return Response.success(chatRooms);
    }
}
