package com.ssafy.alttab.drawing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String DRAWING_KEY_PREFIX = "drawing:room:";
    private static final String PARTICIPANT_KEY_PREFIX = "participant:room:";
    private static final long TIMEOUT_MINUTES = 10;

    public void sendDrawing(Long studyId, Long problemId, String drawingData) {

        String drawingKey = DRAWING_KEY_PREFIX + studyId + "_" + problemId;
        redisTemplate.opsForValue().set(drawingKey, drawingData);
        redisTemplate.expire(drawingKey, TIMEOUT_MINUTES, TimeUnit.MINUTES);

        redisTemplate.convertAndSend(drawingKey, drawingData);
    }

    public void saveParticipant(Long studyId, Long problemId, String userId) {

        String participantKey = PARTICIPANT_KEY_PREFIX + studyId + "_" + problemId;
        redisTemplate.opsForSet().add(participantKey, userId);
        redisTemplate.expire(participantKey, TIMEOUT_MINUTES, TimeUnit.MINUTES);

        Long participantCount = redisTemplate.opsForSet().size(participantKey);
        Optional.ofNullable(participantCount)
                .filter(count -> count >= 2)
                .map(count -> getDrawing(studyId, problemId))
                .ifPresent(savedDrawing -> redisTemplate.convertAndSend(participantKey, savedDrawing));
    }

    private String getDrawing(Long studyId, Long problemId) {

        String drawingKey = DRAWING_KEY_PREFIX + studyId + "_" + problemId;

        return redisTemplate.opsForValue().get(drawingKey);
    }
}
