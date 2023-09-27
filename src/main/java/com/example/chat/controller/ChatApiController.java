package com.example.chat.controller;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.domain.ChatParticipant;
import com.example.chat.domain.ChatRoom;
import com.example.chat.domain.result.Response;
import com.example.chat.dto.ChatListResponseDto;
import com.example.chat.dto.ChatSendRequestDto;
import com.example.chat.dto.TokenDto;
import com.example.chat.dto.chat.*;
import com.example.chat.exception.ErrorCode;
import com.example.chat.service.ChatRoomService;
import com.example.chat.service.ChatService;
import com.example.chat.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
@Slf4j
public class ChatApiController {
    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations template;
    private final ChatService chatService;
    private final UserService userService;

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
     * 채팅 생성 - 채팅 입력 후 전송
     */
    @PostMapping
    public ResponseEntity makeChat(HttpServletRequest request, @RequestBody ChatSendRequestDto requestDto) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replace("Bearer ", "");
        Long senderId = JwtUtil.getId(token); // 보낸이 id
        try {
            chatService.createChat(senderId, requestDto);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("e,getMessage(): ", e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 채팅 내역 조회 - 채팅방 입장
     * */
    @GetMapping("/{chatRoomId}")
    public ResponseEntity getChatList(HttpServletRequest request, @PathVariable("chatRoomId") Long chatRoomId) {
        Long loginedUserId = userService.getUserIdFromTokenInRequest(request);
        if (loginedUserId == 0L) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        ChatListResponseDto chatListResponseDto = chatService.getListWithChatRoomId(loginedUserId, chatRoomId);
        if (chatListResponseDto == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(chatListResponseDto, HttpStatus.OK);
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
