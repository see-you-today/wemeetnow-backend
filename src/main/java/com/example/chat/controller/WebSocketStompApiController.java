package com.example.chat.controller;

import com.example.chat.dto.ChatSendRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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
    private static final Set<String> SESSION_IDS = new HashSet<>();
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener(SessionConnectedEvent.class)
    public void onConnect(SessionConnectEvent event) {
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        SESSION_IDS.add(sessionId);
        log.info("[connect] connections : {}", SESSION_IDS.size());
    }

    @MessageMapping("/send-chat") // destination: "/app/send-chat" 일 경우 호출뒴
//    @SendTo("/sub/message" + chatRoomId)
    public void sendChat(@Payload ChatSendRequestDto requestDto) {
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

}
