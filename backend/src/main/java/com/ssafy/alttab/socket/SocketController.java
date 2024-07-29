package com.ssafy.alttab.socket;

import com.ssafy.alttab.drawing.dto.DrawingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/socket")
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessageSendingOperations template;

    // 그림 데이터 송신 및 수신, 클라이언트 /pub/draw 요청
    @MessageMapping("/draw")
    public void messageDrawingData(@RequestBody DrawingRequestDto drawingRequestDto) {
        // 데이터를 해당 방 구독자들에게 전송
        template.convertAndSend("/sub/draw/" + drawingRequestDto.getDrawingId(), drawingRequestDto);
    }
}