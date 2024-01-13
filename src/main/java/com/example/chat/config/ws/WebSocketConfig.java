package com.example.chat.config.ws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketInterceptor webSocketInterceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("registerStompEndpoints() / enter");
        // 웹 소캣의 엔드포인트: "/ws-stomp"
//        registry.addEndpoint("/ws-stomp")
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
//                .setAllowedOrigins("http://localhost:3000")
//                .withSockJS();
    }

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        log.info("configureMessageBroker() / enter");
//        registry.setCacheLimit(4096);
//        registry.enableSimpleBroker("/sub", "/queue", "/topic", "/participants"); // spring에서 제공하는 내장브로커 사용(추후 RabbitMQ, Kafka같은 브로커를 사용할 수도 있음)
//        // 컨벤션으로 /queue는 메시지가 1:1로 통신할 경우, /topic은 1:N으로 통신할 경우
//        registry.setApplicationDestinationPrefixes("/app", "/pub");
//        /**
//         *  '/sub'가 prefix로 붙은 destination의 클라이언트에게 메시지를 보낼 수 있도록 SimpleBroker를 등록함.
//         *  '/app'가 prefix로 붙은 메시지들은 @MessageMapping이 붙은 method로 바운드됨.
//         * */
//    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/send","/broadcast", "/sub/chat-room/1", "/sub/chat-room/*");
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        log.info("configureClientInboundChannel() / enter");
//        registration.interceptors(webSocketInterceptor);
//    }
}
