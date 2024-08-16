package com.ssafy.alttab.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 메시지 브로커 설정
     * 메시지 라우팅을 위한 간단한 브로커를 활성화하고, 메시지 목적지의 접두사를 설정
     *
     * @param registry MessageBrokerRegistry 객체
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    /**
     * STOMP 엔드포인트를 등록
     * 클라이언트가 WebSocket 연결을 생성하기 위한 엔드포인트를 정의하고, CORS 설정을 적용
     *
     * @param registry StompEndpointRegistry 객체
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/wss", "/ws")
                .setAllowedOriginPatterns("https://i11a309.p.ssafy.io", "http://localhost:3000")
                .withSockJS();
    }
}