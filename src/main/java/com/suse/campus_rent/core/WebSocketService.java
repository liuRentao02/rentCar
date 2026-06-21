package com.suse.campus_rent.core;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 发送消息给指定用户（点对点）
     *
     * @param userId      接收者用户ID
     * @param destination 子目的地（如 /queue/messages）
     * @param payload     消息体
     */
    public void sendToUser(Long userId, String destination, Object payload) {
        // 构建完整目的地：/queue/messages/123
        String fullDestination = destination + "/" + userId;
        messagingTemplate.convertAndSend(fullDestination, payload);
    }
}