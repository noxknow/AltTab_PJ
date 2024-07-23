package com.ssafy.alt_tab.drawing.service;

import com.ssafy.alt_tab.drawing.dto.DrawingRequestDto;
import com.ssafy.alt_tab.drawing.dto.DrawingResponseDto;
import com.ssafy.alt_tab.drawing.entity.Drawing;
import com.ssafy.alt_tab.drawing.repository.DrawingRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final DrawingRepository drawingRepository;
    private final RedissonClient redissonClient;
    private final RedisTemplate<Object, Object> redisTemplate;
    private static final String DRAWING_ID_KEY = "drawing:id";

    public ResponseEntity<String> saveDrawing(DrawingRequestDto drawingRequestDto) {
        try {
            Long id = redisTemplate.opsForValue().increment(DRAWING_ID_KEY, 1);

            if (id == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate ID");
            }

            redisTemplate.opsForValue().set(id.toString(), drawingRequestDto.getDrawingData());

            return ResponseEntity.ok().body("save success with ID: " + id);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save drawing");
        }
    }

    public ResponseEntity<DrawingResponseDto> loadDrawing() {

        String drawingData = drawingRepository.findTopByOrderByDrawingIdDesc()
                .map(Drawing::getDrawingData)
                .orElse("");

        DrawingResponseDto drawingResponseDto = DrawingResponseDto.builder()
                .drawingData(drawingData)
                .build();

        return ResponseEntity.ok().body(drawingResponseDto);
    }
}
