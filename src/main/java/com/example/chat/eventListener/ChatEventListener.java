package com.example.chat.eventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ChatEventListener {
    private final SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebScoketConnectListener(SessionConnectedEvent event) {
        log.info("===새로운 웹 소켓 연결===");
    }
}
