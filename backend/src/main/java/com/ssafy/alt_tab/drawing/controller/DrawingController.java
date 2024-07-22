package com.ssafy.alt_tab.drawing.controller;

import com.ssafy.alt_tab.drawing.dto.DrawingRequestDto;
import com.ssafy.alt_tab.drawing.dto.DrawingResponseDto;
import com.ssafy.alt_tab.drawing.service.DrawingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DrawingController {

    private final DrawingService drawingService;

    @PostMapping("/save")
    public ResponseEntity<String> saveDrawing(@RequestBody DrawingRequestDto drawingRequestDto) {

        return drawingService.saveDrawing(drawingRequestDto);
    }

    @GetMapping("/load")
    public ResponseEntity<DrawingResponseDto> loadDrawing() {

        return drawingService.loadDrawing();
    }
}