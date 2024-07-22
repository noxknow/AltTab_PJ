package com.ssafy.alt_tab.drawing.service;

import com.ssafy.alt_tab.drawing.dto.DrawingRequestDto;
import com.ssafy.alt_tab.drawing.dto.DrawingResponseDto;
import com.ssafy.alt_tab.drawing.entity.Drawing;
import com.ssafy.alt_tab.drawing.repository.DrawingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final DrawingRepository drawingRepository;

    public ResponseEntity<String> saveDrawing(DrawingRequestDto drawingRequestDto) {

        Drawing drawing = Drawing.builder()
                .drawingData(drawingRequestDto.getDrawingData())
                .build();

        drawingRepository.save(drawing);

        return ResponseEntity.ok().body("save success");
    }

    public ResponseEntity<DrawingResponseDto> loadDrawing() {

        String drawingData = drawingRepository.findTopByOrderByIdDesc()
                .map(Drawing::getDrawingData)
                .orElse("");

        DrawingResponseDto drawingResponseDto = DrawingResponseDto.builder()
                .drawingData(drawingData)
                .build();

        return ResponseEntity.ok().body(drawingResponseDto);
    }
}
