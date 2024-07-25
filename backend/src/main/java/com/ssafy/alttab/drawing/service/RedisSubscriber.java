package com.ssafy.alt_tab.drawing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.alt_tab.drawing.dto.DrawingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            // ChatMessage 객채로 맵핑
            DrawingRequestDto roomMessage = objectMapper.readValue(publishMessage, DrawingRequestDto.class);
            // Websocket 구독자에게 채팅 메시지 Send
            messagingTemplate.convertAndSend("/sub/api/v1/rooms/" + roomMessage.getRoomId(), roomMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}