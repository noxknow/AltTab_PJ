package com.ssafy.alttab.problem.controller;

import com.ssafy.alttab.common.exception.ProblemNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.problem.dto.AddProblemsRequestDto;
import com.ssafy.alttab.problem.dto.RemoveProblemsRequestDto;
import com.ssafy.alttab.problem.dto.SearchProblemListDto;
import com.ssafy.alttab.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problem")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/search/{problemInfo}")
    public ResponseEntity<SearchProblemListDto> searchProblems(@PathVariable("problemInfo") String problemInfo,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "15") int size) {

        return problemService.searchProblems(problemInfo, page, size);
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
