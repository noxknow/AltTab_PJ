package com.ssafy.alttab.community.controller;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.community.dto.CommunityMainResponseDto;
import com.ssafy.alttab.community.dto.TopFollowerDto;
import com.ssafy.alttab.community.dto.TopSolverDto;
import com.ssafy.alttab.community.service.CommunityService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/main")
    public ResponseEntity<CommunityMainResponseDto> getCommunityMain(@AuthenticationPrincipal UserDetails userDetails) throws MemberNotFoundException {
        CommunityMainResponseDto response = communityService.getCommunityMain(userDetails.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/top/follower")
    public ResponseEntity<List<TopFollowerDto>> getTopFollower(@AuthenticationPrincipal UserDetails userDetails) throws MemberNotFoundException {
        return new ResponseEntity<>(communityService.getTopFollowerStudyList(userDetails.getUsername()), HttpStatus.OK);
    }

    @GetMapping("/top/solve")
    public ResponseEntity<List<TopSolverDto>> getTopSolver(@AuthenticationPrincipal UserDetails userDetails) throws MemberNotFoundException {
        return new ResponseEntity<>(communityService.getTopSolversStudyList(userDetails.getUsername()), HttpStatus.OK);
    }

    @GetMapping("/my/follow")
    public ResponseEntity<?> getFollowingStudy(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(communityService.getFollowingStudy(userDetails.getUsername()), HttpStatus.OK);
    }

    @PostMapping("/follow/{studyId}")
    public ResponseEntity<?> followStudy(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long studyId) throws StudyNotFoundException, MemberNotFoundException {
        return new ResponseEntity<>(communityService.followStudy(userDetails.getUsername(), studyId), HttpStatus.OK);
    }


}