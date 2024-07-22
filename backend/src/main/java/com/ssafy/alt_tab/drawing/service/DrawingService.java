package com.ssafy.alt_tab.drawing.service;

import com.ssafy.alt_tab.drawing.entity.Drawing;
import com.ssafy.alt_tab.drawing.repository.DrawingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final DrawingRepository drawingRepository;

    public ResponseEntity<String> saveDrawing(String drawingData) {

        Drawing drawing = Drawing.builder()
                .drawingData(drawingData)
                .build();

        drawingRepository.save(drawing);

        return ResponseEntity.ok().body("save success");
    }
}
