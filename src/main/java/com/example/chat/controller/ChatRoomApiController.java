package com.example.chat.controller;

import com.example.chat.dto.ChatListResponseDto;
import com.example.chat.dto.chat.ChatRoomResponseDto;
import com.example.chat.service.ChatRoomService;
import com.example.chat.service.ChatService;
import com.example.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomApiController {
    private final ChatService chatService;
    private final UserService userService;

    /**
     * 채팅방 입장해서 채팅목록 조회
     * */
    @GetMapping("/{chatRoomId}")
    public ResponseEntity getChatRoomDetail(
            HttpServletRequest request,
            @PathVariable("chatRoomId") Long chatRoomId) {
        Long loginedUserId = userService.getUserIdFromTokenInRequest(request);
        if (loginedUserId == 0L) {
            return new ResponseEntity(UNAUTHORIZED);
        }
        try {
            ChatListResponseDto chatResponseDtos = chatService.getListWithChatRoomId(loginedUserId, chatRoomId);
            return new ResponseEntity(chatResponseDtos, OK);
        } catch (IllegalArgumentException iae) {
            log.info("raised error = " + iae.getMessage());
            return new ResponseEntity(BAD_REQUEST);
        }
    }
}
