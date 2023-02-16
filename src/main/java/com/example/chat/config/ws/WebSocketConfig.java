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
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new FilterChannelInterceptor());
    }
    /**
     * 2023-02-02 02:24:56.530  INFO 21596 --- [MessageBroker-1] o.s.w.s.c.WebSocketMessageBrokerStats    : WebSocketSession[0 current WS(0)-HttpStream(0)-HttpPoll(0), 0 total, 0 closed abnormally (0 connect failure, 0 send limit, 0 transport error)], stompSubProtocol[processed CONNECT(0)-CONNECTED(0)-DISCONNECT(0)], stompBrokerRelay[null], inboundChannel[pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0], outboundChannel[pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0], sockJsScheduler[pool size = 1, active threads = 1, queued tasks = 0, completed tasks = 0]
     * */
//    @Bean
//    public WebSocketMessageBrokerStats webSocketMessageBrokerStats() {
//        AbstractBrokerMessageHandler relayBean = stompBrokerRelayMessageHandler();
//
//        // Ensure STOMP endpoints are registered
//        stompWebSocketHandlerMapping();
//
//        WebSocketMessageBrokerStats stats = new WebSocketMessageBrokerStats();
//        stats.setSubProtocolWebSocketHandler((SubProtocolWebSocketHandler) subProtocolWebSocketHandler());
//        if (relayBean instanceof StompBrokerRelayMessageHandler) {
//            stats.setStompBrokerRelay((StompBrokerRelayMessageHandler) relayBean);
//        }
//        stats.setInboundChannelExecutor(clientInboundChannelExecutor());
//        stats.setOutboundChannelExecutor(clientOutboundChannelExecutor());
//        stats.setSockJsTaskScheduler(messageBrokerTaskScheduler());
//        return stats;
//    }
}
