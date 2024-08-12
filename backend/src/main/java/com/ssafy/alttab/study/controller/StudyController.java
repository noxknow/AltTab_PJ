package com.ssafy.alttab.study.controller;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.study.dto.MakeStudyRequestDto;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.dto.StudyScheduleRequestDto;
import com.ssafy.alttab.study.service.StudyScheduleService;
import com.ssafy.alttab.study.service.StudyService;
import java.time.LocalDateTime;
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
    private final StudyScheduleService studyScheduleService;

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

    @PostMapping
    public ResponseEntity<?> createStudy(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MakeStudyRequestDto dto) throws MemberNotFoundException {
        return new ResponseEntity<>(studyService.createStudy(userDetails.getUsername(), dto), HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<?> createStudyTest(String username, @RequestBody MakeStudyRequestDto dto) throws MemberNotFoundException {
        return new ResponseEntity<>(studyService.createStudy(username, dto), HttpStatus.OK);
    }

    @GetMapping("/schedule/{id}/{startDate}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id, @PathVariable LocalDateTime startDate){
        return new ResponseEntity<>(studyScheduleService.getStudySchedule(id, startDate), HttpStatus.OK);
    }

    @PostMapping("/schedule/update")
    public ResponseEntity<?> updateSchedule(StudyScheduleRequestDto requestDto){
        return new ResponseEntity<>(studyScheduleService.updateOrCreateStudySchedule(requestDto), HttpStatus.OK);
    }
}
