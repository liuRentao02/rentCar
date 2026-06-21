package com.suse.campus_rent.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(jwtHandshakeInterceptor);  // 添加 JWT 认证拦截器
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 启用 /topic 用于广播（预警），/queue 用于点对点聊天
        registry.enableSimpleBroker("/topic", "/queue");
        // 客户端发送消息的前缀（如 /app/chat）– 本例不使用 STOMP 发送消息，仅用于推送，可以省略
        registry.setApplicationDestinationPrefixes("/app");
    }
}