package com.example.chat.controller.front;

import com.example.chat.domain.ChatRoom;
import com.example.chat.dto.chat.ChatRoomCreateRequestDto;
import com.example.chat.dto.chat.ChatRoomResponseDto;
import com.example.chat.dto.chat.ChatRoomResponseDtoList;
import com.example.chat.service.ChatRoomService;
import com.example.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomApiFrontController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    /**
     * 채팅방 생성
     * */
    @PostMapping
    public ResponseEntity createChatRoom(
            HttpServletRequest request,
            @RequestBody ChatRoomCreateRequestDto requestDto) {
        log.info("ChatRoomCreateRequestDto = " + requestDto);
        Long loginedUserId = userService.getUserIdFromTokenInRequest(request);
        if (loginedUserId == 0L) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        try {
            ChatRoom chatRoom = chatRoomService.createChatRoom(requestDto, loginedUserId);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            e.getStackTrace();
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * 참여중인 채팅방 전체 목록 조회
     */
    @GetMapping
    public ResponseEntity getChatRooms(HttpServletRequest request) {
        Long loginedUserId = userService.getUserIdFromTokenInRequest(request);
        if (loginedUserId == 0L) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        try {
            log.info("loginedUserId: ", loginedUserId);
            ChatRoomResponseDtoList chatRoomResponseDtoList = chatRoomService.getChatRooms(loginedUserId);
            return new ResponseEntity(chatRoomResponseDtoList, HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            iae.getStackTrace();
            iae.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
