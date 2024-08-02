package com.ssafy.alttab.common.realtime.controller;

import com.ssafy.alttab.common.realtime.service.WeeklyStudyUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/realtime")
public class RealtimeUpdateController {
    private final WeeklyStudyUpdateService weeklyStudyUpdateService;

    @GetMapping("/weekly-studies")
    public SseEmitter subscribeToWeeklyStudies() {
        return weeklyStudyUpdateService.subscribe();
    }
}
