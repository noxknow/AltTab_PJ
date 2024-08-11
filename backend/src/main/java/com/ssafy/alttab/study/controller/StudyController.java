package com.ssafy.alttab.study.controller;

import com.ssafy.alttab.common.exception.ProblemNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.problem.dto.AddProblemsRequestDto;
import com.ssafy.alttab.problem.dto.RemoveProblemsRequestDto;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PostMapping("/{studyId}/problem")
    public ResponseEntity<?> addProblems(@PathVariable Long studyId,
                                         @RequestBody AddProblemsRequestDto dto) throws StudyNotFoundException, ProblemNotFoundException {
        studyService.addProblems(studyId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studyId}/problem")
    public ResponseEntity<?> getProblems(@PathVariable Long studyId) throws StudyNotFoundException {
        return new ResponseEntity<>(studyService.getProblems(studyId), HttpStatus.OK);
    }

    @DeleteMapping("/{studyId}/problem")
    public ResponseEntity<?> removeProblems(@PathVariable Long studyId,
                                            @RequestBody RemoveProblemsRequestDto dto) throws StudyNotFoundException {
        studyService.removeProblems(studyId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studyId}/problem/{option}/{target}")
    public ResponseEntity<?> queryProblems(@PathVariable Long studyId,
                                           @PathVariable Long option,
                                           @PathVariable String target) throws StudyNotFoundException {
        return new ResponseEntity<>(studyService.queryProblems(studyId, option, target), HttpStatus.OK);
    }

    @GetMapping("/{studyId}/problem/weekly")
    public ResponseEntity<?> weeklyProblems(@PathVariable Long studyId,
                                            @RequestParam("today") LocalDate today) throws StudyNotFoundException {
        return new ResponseEntity<>(studyService.weeklyProblems(studyId, today), HttpStatus.OK);
    }

}
