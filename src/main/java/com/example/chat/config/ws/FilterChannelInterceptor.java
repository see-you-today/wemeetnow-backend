package com.example.chat.config.ws;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.config.jwt.JwtUtilEnums;
import com.example.chat.exception.ApplicationException;
import com.example.chat.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import static com.example.chat.config.jwt.JwtUtilEnums.*;
import static com.example.chat.exception.ErrorCode.*;
import static org.springframework.security.config.Elements.JWT;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class FilterChannelInterceptor implements ChannelInterceptor {
    @SneakyThrows
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            log.info("enter preSend()");
            StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            log.info("headerAccessor = ", headerAccessor);
            assert headerAccessor != null;
            if (headerAccessor.getCommand() == StompCommand.CONNECT) { // 연결 시에한 header 확인
                String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
                if (authorizationHeader == null) {
                    log.error("preSend/header가 없는 요청입니다");
                    throw new ApplicationException(INVALID_TOKEN);
                }
                String token = "";
                if (authorizationHeader.startsWith("Bearer ")) {
                    token = authorizationHeader.replace("Bearer ", "");
                }
                try {
                    Long userId = JwtUtil.getId(token);
                    headerAccessor.addNativeHeader("User", String.valueOf(userId));
                } catch (ExpiredJwtException e) {
                    e.printStackTrace();
                } catch (UnsupportedJwtException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            log.info("preSend() / return message");
            return message;
        }
    }
}
