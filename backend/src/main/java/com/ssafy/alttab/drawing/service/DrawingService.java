package com.ssafy.alttab.drawing.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String DRAWING_KEY_PREFIX = "drawing:room:";

    public void saveDrawing(Long roomId, String drawingData) {
        String key = DRAWING_KEY_PREFIX + roomId;
        redisTemplate.opsForValue().set(key, drawingData);

        redisTemplate.convertAndSend("drawing:room:" + roomId, drawingData);
    }

    public ResponseEntity<String> loadLatestDrawing(Long roomId) {
        String key = DRAWING_KEY_PREFIX + roomId;
        String drawingData = redisTemplate.opsForValue().get(key);

        if (drawingData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No drawing found for room " + roomId);
        }

        return ResponseEntity.ok(drawingData);
    }
}
