package com.ssafy.alttab.drawing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String DRAWING_KEY_PREFIX = "drawing:room:";

    public void saveDrawing(Long studyId, Long problemId, String drawingData) {
        String key = DRAWING_KEY_PREFIX + studyId + "_" + problemId;
        redisTemplate.opsForValue().set(key, drawingData);

        redisTemplate.convertAndSend("drawing:room:" + studyId + "_" + problemId, drawingData);
    }
}
