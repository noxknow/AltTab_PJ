package com.ssafy.alttab.drawing.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final RedissonClient redissonClient;
    private final RedisTemplate<Object, Object> redisTemplate;
    private static final String DRAWING_ID_KEY = "drawing:id";
    private static final String LATEST_DRAWING_ID_KEY = "drawing:latest";

    public ResponseEntity<String> saveDrawing(String drawingData) {
        try {
            Long id = redisTemplate.opsForValue().increment(DRAWING_ID_KEY, 1);

            if (id == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate ID");
            }

            redisTemplate.opsForValue().set(id.toString(), drawingData);
            redisTemplate.opsForValue().set(LATEST_DRAWING_ID_KEY, id.toString());

            return ResponseEntity.ok().body("save success with ID: " + id);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save drawing");
        }
    }

    public ResponseEntity<String> loadLatestDrawing() {
        try {
            String latestId = (String) redisTemplate.opsForValue().get(LATEST_DRAWING_ID_KEY);

            if (latestId == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No drawings found");
            }

            String drawingData = (String) redisTemplate.opsForValue().get(latestId);

            if (drawingData == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Drawing not found");
            }

            return ResponseEntity.ok().body(drawingData);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load drawing");
        }
    }
}
