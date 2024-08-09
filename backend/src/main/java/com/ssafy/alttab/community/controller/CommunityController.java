package com.ssafy.alttab.community.controller;

import com.ssafy.alttab.community.dto.CommunityMainResponseDto;
import com.ssafy.alttab.community.dto.TopFollowerDto;
import com.ssafy.alttab.community.dto.TopSolverDto;
import com.ssafy.alttab.community.service.CommunityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;


    @GetMapping("/")
    public ResponseEntity<CommunityMainResponseDto> getCommunityMain() {
        CommunityMainResponseDto response = communityService.getCommunityMain();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/top/follower")
    public ResponseEntity<List<TopFollowerDto>> getTopFollower() {
        return new ResponseEntity<>(communityService.getTopFollowerStudys(), HttpStatus.OK);
    }

    @GetMapping("/top/solve")
    public ResponseEntity<List<TopSolverDto>> getTopSolver() {
        return new ResponseEntity<>(communityService.getTopSolvers(), HttpStatus.OK);
    }
}