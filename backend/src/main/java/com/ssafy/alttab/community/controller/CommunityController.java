package com.ssafy.alttab.community.controller;

import com.ssafy.alttab.common.realtime.service.WeeklyStudyUpdateService;
import com.ssafy.alttab.community.dto.CommunityMainResponseDto;
import com.ssafy.alttab.community.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/community")
public class CommunityController {

    private final CommunityService communityService;
    private final WeeklyStudyUpdateService weeklyStudyUpdateService;

    @Autowired
    public CommunityController(CommunityService communityService, WeeklyStudyUpdateService weeklyStudyUpdateService) {
        this.communityService = communityService;
        this.weeklyStudyUpdateService = weeklyStudyUpdateService;
    }

    @GetMapping("/")
    public ResponseEntity<CommunityMainResponseDto> getCommunityMain() {
        CommunityMainResponseDto response = communityService.getCommunityMain();
        return ResponseEntity.ok(response);
    }
}