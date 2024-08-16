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

    /**
     * 그림 데이터를 Redis 에 저장하고 해당 데이터를 구독자들에게 전송
     *
     * @param studyId 스터디 고유 아이디
     * @param problemId 문제 고유 아이디
     * @param drawingData 그림 데이터
     */
    public void sendDrawing(Long studyId, Long problemId, String drawingData) {

        String drawingKey = DRAWING_KEY_PREFIX + studyId + "_" + problemId;
        redisTemplate.opsForValue().set(drawingKey, drawingData);
        redisTemplate.expire(drawingKey, TIMEOUT_MINUTES, TimeUnit.MINUTES);

        redisTemplate.convertAndSend(drawingKey, drawingData);
    }

    /**
     * 참가자 정보를 Redis 에 저장하고, 필요한 경우 저장된 그림 데이터를 구독자들에게 전송
     *
     * @param studyId 스터디 고유 아이디
     * @param problemId 문제 고유 아이디
     * @param userId 참가자 고유 아이디
     */
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

    /**
     * Redis 에서 저장된 그림 데이터를 가져옴
     *
     * @param studyId 스터디 고유 아이디
     * @param problemId 문제 고유 아이디
     * @return 저장된 그림 데이터
     */
    private String getDrawing(Long studyId, Long problemId) {

        String drawingKey = DRAWING_KEY_PREFIX + studyId + "_" + problemId;

        return redisTemplate.opsForValue().get(drawingKey);
    }
}
