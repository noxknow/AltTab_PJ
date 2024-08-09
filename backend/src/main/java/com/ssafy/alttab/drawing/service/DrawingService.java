package com.ssafy.alttab.drawing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String DRAWING_KEY_PREFIX = "drawing:room:";
    private static final String PARTICIPANT_KEY_PREFIX = "participant:room:";

    public void sendDrawing(Long studyId, Long problemId, String drawingData) {
        String key = DRAWING_KEY_PREFIX + studyId + "_" + problemId;
        redisTemplate.opsForValue().set(key, drawingData);

        redisTemplate.convertAndSend(key, drawingData);
    }

    public void saveParticipant(Long studyId, Long problemId, String userId) {

        String participantKey = PARTICIPANT_KEY_PREFIX + studyId + "_" + problemId;
        redisTemplate.opsForSet().add(participantKey, userId);

        Long participantCount = redisTemplate.opsForSet().size(participantKey);
        Optional.ofNullable(participantCount)
                .filter(count -> count >= 2)
                .map(count -> getDrawing(studyId, problemId))
                .ifPresent(savedDrawing -> redisTemplate.convertAndSend(participantKey, savedDrawing));
    }

    private String getDrawing(Long studyId, Long problemId) {
        String key = DRAWING_KEY_PREFIX + studyId + "_" + problemId;

        return redisTemplate.opsForValue().get(key);
    }
}
