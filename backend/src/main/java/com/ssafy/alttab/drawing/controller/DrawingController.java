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

    /**
     * 같은 방을 구독한 구독자들에게 그림데이터 전송
     *
     * @param studyId 스터디 고유 아이디
     * @param problemId 문제 고유 아이디
     * @param drawingData 그림 데이터
     */
    @MessageMapping("/api/v1/rooms/{studyId}/{problemId}")
    public void sendDrawing(@DestinationVariable("studyId") Long studyId,
                            @DestinationVariable("problemId") Long problemId,
                            @Payload String drawingData) {

        drawingService.sendDrawing(studyId, problemId, drawingData);
    }

    /**
     * 방을 나갔다 들어온 구독자에게 이전까지의 그림 데이터 전송
     *
     * @param studyId 스터디 고유 아이디
     * @param problemId 문제 고유 아이디
     * @param userId 들어온 사용자를 구분하기 위한 UUID
     */
    @MessageMapping("/api/v1/rooms/{studyId}/{problemId}/enter")
    public void enterRoom(@DestinationVariable("studyId") Long studyId,
                          @DestinationVariable("problemId") Long problemId,
                          @Payload String userId) {

        drawingService.saveParticipant(studyId, problemId, userId);
    }
}