package com.ssafy.alttab.study.controller;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.service.StudyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
@Slf4j
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/{studyId}")
    public ResponseEntity<?> getStudyInfo(@PathVariable Long studyId) throws StudyNotFoundException {
        return new ResponseEntity<>(studyService.getStudyInfo(studyId), HttpStatus.OK);
    }

    @GetMapping("/{studyId}/members")
    public ResponseEntity<?> getStudyMembers(@PathVariable Long studyId) throws StudyNotFoundException {
        return new ResponseEntity<>(studyService.getStudyMembers(studyId), HttpStatus.OK);
    }

    @PutMapping("/{studyId}")
    public ResponseEntity<?> updateStudyInfo(@PathVariable Long studyId,
                                             @RequestBody StudyInfoRequestDto dto) throws StudyNotFoundException {
        return new ResponseEntity<>(studyService.updateStudyInfo(studyId, dto), HttpStatus.OK);
    }

    @GetMapping("/{studyId}/follow")
    public ResponseEntity<?> followStudy(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable Long studyId) throws StudyNotFoundException, MemberNotFoundException {
        return new ResponseEntity<>(studyService.followStudy(userDetails.getUsername(), studyId), HttpStatus.OK);
    }

    @GetMapping("/{studyId}/unfollow")
    public ResponseEntity<?> unfollowStudy(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable Long studyId) throws EntityNotFoundException {
        return new ResponseEntity<>(studyService.unfollowStudy(userDetails.getUsername(), studyId), HttpStatus.OK);
    }
}
