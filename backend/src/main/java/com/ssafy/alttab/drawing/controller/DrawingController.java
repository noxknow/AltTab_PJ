package com.ssafy.alt_tab.drawing.controller;

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
    public ResponseEntity<String> saveDrawing(@RequestBody String drawingData) {

        return drawingService.saveDrawing(drawingData);
    }

    @GetMapping("/load")
    public ResponseEntity<String> loadDrawing() {

        return drawingService.loadLatestDrawing();
    }
}