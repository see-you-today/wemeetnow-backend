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
    private final FilterChannelInterceptor filterChannelInterceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("registerStompEndpoints() / enter");
        // 웹 소캣의 엔드포인트: "/ws"
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS()
        ;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("configureMessageBroker() / enter");
        registry.setCacheLimit(4096);
        registry.enableSimpleBroker("/sub");
//        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(filterChannelInterceptor);
    }
}
