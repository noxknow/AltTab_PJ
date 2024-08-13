package com.ssafy.alttab.drawing.service;

import static com.ssafy.alttab.common.enums.ErrorCode.CONNECTION_REFUSED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.alttab.common.exception.AppException;
import com.ssafy.alttab.drawing.dto.DrawingRequestDto;
import lombok.NonNull;
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
    private final RedisTemplate<Object, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis 에서 메시지를 수신했을 때 실행되는 메소드
     *
     * @param message Redis 에서 수신된 메시지
     * @param pattern Redis 채널 패턴 (사용되지 않음)
     */
    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            DrawingRequestDto drawingRequestDto = objectMapper.readValue(publishMessage, DrawingRequestDto.class);
            messagingTemplate.convertAndSend(
                    "/sub/api/v1/rooms" + "/" + drawingRequestDto.getStudyId() + "/" + drawingRequestDto.getProblemId(),
                    drawingRequestDto);
        } catch (Exception e) {
            throw new AppException(CONNECTION_REFUSED);
        }
    }
}