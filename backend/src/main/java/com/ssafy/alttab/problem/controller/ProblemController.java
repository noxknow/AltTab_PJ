package com.ssafy.alttab.problem.controller;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.ProblemNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.problem.dto.AddProblemsRequestDto;
import com.ssafy.alttab.problem.dto.RemoveProblemsRequestDto;
import com.ssafy.alttab.problem.dto.SearchProblemListResponseDto;
import com.ssafy.alttab.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problem")
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

    @GetMapping("/{studyId}/{problemInfo}")
    public ResponseEntity<SearchProblemListResponseDto> searchProblems(@PathVariable("studyId") Long studyId,
                                                                       @PathVariable("problemInfo") String problemInfo) throws StudyNotFoundException, ProblemNotFoundException {

        return problemService.searchProblems(studyId, problemInfo);
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

    @PostMapping("/solve/{memberId}/{studyId}/{problemId}")
    public ResponseEntity<?> solveProblem(@PathVariable Long memberId, @PathVariable Long studyId, @PathVariable Long problemId) throws StudyNotFoundException {
        problemService.solveProblem(memberId, studyId, problemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{studyId}/{problemId}")
    public ResponseEntity<?> statusProblem(@PathVariable Long studyId, @PathVariable Long problemId){
        return new ResponseEntity<>(problemService.statusProblem(studyId, problemId), HttpStatus.OK);
    }
}
