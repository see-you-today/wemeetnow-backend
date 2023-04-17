package com.example.chat.config.ws;

import com.example.chat.config.jwt.JwtUtil;
import com.example.chat.domain.enums.Role;
import com.example.chat.exception.ApplicationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static com.example.chat.exception.ErrorCode.*;

@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
@RequiredArgsConstructor
public class FilterChannelInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    Long userId = 0L;
//    @SneakyThrows
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            log.info("preSend() / message: " + message.toString());
            log.info("channel: ", channel);
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
                } else {
                    log.error("Authorization 헤더 형식이 틀립니다. : [{}]", authorizationHeader);
                    throw new ApplicationException(INVALID_TOKEN);
                }
                try {
                    this.userId = JwtUtil.getId(token);
                    headerAccessor.addNativeHeader("User", String.valueOf(this.userId));
                } catch (ExpiredJwtException e) {
                    e.getMessage();
                    e.printStackTrace();
                    throw new ApplicationException(INVALID_TOKEN);
                } catch (UnsupportedJwtException e) {
                    e.getMessage();
                    e.printStackTrace();
                    throw new ApplicationException(INVALID_TOKEN);
                }
                if (jwtUtil.validateToken(token)) {
                    this.setAuthentication(message, headerAccessor);
                }
            }
        } catch (Exception e){
            e.getMessage();
            e.printStackTrace();
            log.error("JWT 에러 발생!");
        } finally {
            log.info("preSend() / finally / return message");
            return message;
        }
    }

    private void setAuthentication(Message<?> message, StompHeaderAccessor headerAccessor) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(this.userId, null, List.of(new SimpleGrantedAuthority(Role.ROLE_USER.name())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        headerAccessor.setUser(authentication);
    }
}
