package com.ssafy.alttab.problem.controller;

import com.ssafy.alttab.common.exception.ProblemNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.problem.dto.AddProblemsRequestDto;
import com.ssafy.alttab.problem.dto.RemoveProblemsRequestDto;
import com.ssafy.alttab.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/{studyId}")
    public ResponseEntity<?> addProblems(@PathVariable Long studyId,
                                         @RequestBody AddProblemsRequestDto dto) throws StudyNotFoundException, ProblemNotFoundException {
        problemService.addProblems(studyId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<?> getProblems(@PathVariable Long studyId) throws StudyNotFoundException {
        return new ResponseEntity<>(problemService.getProblems(studyId), HttpStatus.OK);
    }

    @DeleteMapping("/{studyId}")
    public ResponseEntity<?> removeProblems(@PathVariable Long studyId,
                                            @RequestBody RemoveProblemsRequestDto dto) throws StudyNotFoundException {
        problemService.removeProblems(studyId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studyId}/{option}/{target}")
    public ResponseEntity<?> queryProblems(@PathVariable Long studyId,
                                           @PathVariable Long option,
                                           @PathVariable String target) throws StudyNotFoundException {
        return new ResponseEntity<>(problemService.queryProblems(studyId, option, target), HttpStatus.OK);
    }

    @GetMapping("/{studyId}/weekly")
    public ResponseEntity<?> weeklyProblems(@PathVariable Long studyId,
                                            @RequestParam("today") LocalDate today) throws StudyNotFoundException {
        return new ResponseEntity<>(problemService.weeklyProblems(studyId, today), HttpStatus.OK);
    }
}
