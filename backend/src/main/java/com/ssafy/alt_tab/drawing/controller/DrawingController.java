package com.ssafy.alt_tab.drawing.controller;

import com.ssafy.alt_tab.drawing.dto.DrawingRequestDto;
import com.ssafy.alt_tab.drawing.service.DrawingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DrawingController {

    private final DrawingService drawingService;

    @MessageMapping("/api/v1/rooms/{roomId}")
    public void saveDrawing(@DestinationVariable("roomId") Long roomId, @Payload String drawingData) {

        drawingService.saveDrawing(roomId, drawingData);
    }

    @GetMapping("/api/v1/load/rooms/{roomId}")
    public ResponseEntity<String> loadDrawing(@PathVariable("roomId") Long roomId) {

        return drawingService.loadLatestDrawing(roomId);
    }
}