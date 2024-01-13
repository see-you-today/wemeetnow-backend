package com.example.chat.controller;

import com.example.chat.dto.ChatSendRequestDto;
import com.example.chat.dto.chat.ChatDto;
import com.example.chat.dto.chat.ChatRequestDto;
import com.example.chat.dto.chat.Msg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebSocketStompApiController {
    private final SimpMessageSendingOperations template;
    private static final Set<String> SESSION_IDS = new HashSet<>();
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener(SessionConnectedEvent.class)
    public void onConnect(SessionConnectEvent event) {
//        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        String sessionId = String.valueOf(event.getMessage().getHeaders().get("simpSessionId"));
        log.info("sessionId : {}", sessionId);
        SESSION_IDS.add(sessionId);
        log.info("[connect] connections : {}", SESSION_IDS.size());
    }

    @MessageMapping("/send-chat") // destination: "/app/send-chat" 일 경우 호출뒴
//    @SendTo("/sub/message" + chatRoomId)
    public void sendChat(@Payload ChatSendRequestDto requestDto) {
        log.info("ChatSendRequestDto.getChatRoomId : [{}]", requestDto.getChatRoomId());
        log.info("ChatSendRequestDto.getContent : [{}]", requestDto.getContent());
        log.info("ChatSendRequestDto: ", requestDto);
        messagingTemplate.convertAndSend("/sub/chat-room/" + requestDto.getChatRoomId(), requestDto.getContent()); // "/sub/message"를 구독하고 있는 클라이언트들에게 request.getContent()가 보내진다.
    }

    @SubscribeMapping("/sub/chat-room/{chatRoomId}")
    public ResponseEntity chatInRoom(@Payload ChatSendRequestDto requestDto, @DestinationVariable Long chatRoomId) {
        log.info("[send message in chatRoom] roomId: {}, message: {}", chatRoomId, requestDto.getContent());
        return new ResponseEntity(HttpStatus.OK);
    }

    @EventListener(SessionDisconnectEvent.class)
    public void onDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        SESSION_IDS.remove(sessionId);
        log.info("[disconnect] connections : {}", SESSION_IDS.size());
    }
    @MessageMapping("/receiveall") // 모두에게 전송
    public void receiveall(Msg msg, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println(msg);
        messagingTemplate.convertAndSend("/send", msg);
    }
    /**
     * 메시지 보내기
     * */
    @MessageMapping("/send-message")
    public void sendMessage(@Payload ChatRequestDto requestDto) {
        // "/sub/chat/{channelId}" 채널을 구독 중인 클라이언트에게 메시지를 전송
        log.info("requestDto = " + requestDto);
        template.convertAndSend("/sub/chat/room/" + requestDto.getRoomId(), requestDto);
    }

    @MessageMapping("/receive-to") // 특정 Id에게 전송
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
