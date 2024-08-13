package com.ssafy.alttab.study.controller;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.study.dto.DeleteScheduleProblemRequestDto;
import com.ssafy.alttab.study.dto.MakeStudyRequestDto;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.dto.StudyScheduleRequestDto;
import com.ssafy.alttab.study.service.StudyScheduleService;
import com.ssafy.alttab.study.service.StudyService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{studyId}/score")
    public ResponseEntity<?> getStudyScore(@PathVariable Long studyId) throws StudyNotFoundException {
        return new ResponseEntity<>(studyService.getStudyScore(studyId), HttpStatus.OK);
    }

    @GetMapping("/schedule/{id}/{deadline}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id, @PathVariable LocalDate deadline) throws StudyNotFoundException {
        return new ResponseEntity<>(studyScheduleService.getStudySchedule(id, deadline), HttpStatus.OK);
    }

    @PostMapping("/schedule/update")
    public ResponseEntity<?> updateSchedule(@RequestBody StudyScheduleRequestDto requestDto){
        return new ResponseEntity<>(studyScheduleService.updateOrCreateStudySchedule(requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/schedule/problem/delete")
    public ResponseEntity<?> deleteScheduleProblem(@RequestBody DeleteScheduleProblemRequestDto requestDto){
        return new ResponseEntity<>(studyScheduleService.deleteStudyProblems(requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/schedule/{studyId}/{deadline}")
    public ResponseEntity<?> deleteStudySchedule(@PathVariable Long studyId, @PathVariable LocalDate deadline){
        return new ResponseEntity<>(studyScheduleService.deleteStudySchedule(studyId, deadline), HttpStatus.OK);
    }

    @GetMapping("/schedule/deadline/{yearMonth}")
    public ResponseEntity<?> getDeadlines(@PathVariable LocalDate yearMonth){
        return new ResponseEntity<>(studyScheduleService.findDeadlines(yearMonth), HttpStatus.OK);
    }
}
