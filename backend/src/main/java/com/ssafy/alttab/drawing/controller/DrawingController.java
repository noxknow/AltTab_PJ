package com.ssafy.alttab.drawing.controller;

import com.ssafy.alttab.drawing.service.DrawingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DrawingController {

    private final DrawingService drawingService;

    @MessageMapping("/api/v1/rooms/{studyId}/{problemId}")
    public void saveDrawing(@DestinationVariable("studyId") Long studyId,
                            @DestinationVariable("problemId") Long problemId, @Payload String drawingData) {

        drawingService.saveDrawing(studyId, problemId, drawingData);
    }
}