package com.ssafy.alttab.solution.controller;

import com.ssafy.alttab.common.exception.DocumentNotFoundException;
import com.ssafy.alttab.solution.dto.SolutionRequestDto;
import com.ssafy.alttab.solution.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/solutions")
@RequiredArgsConstructor
public class SolutionController {
    private final SolutionService solutionService;

    @GetMapping("/{studyId}/{problemId}")
    public ResponseEntity<?> getSolution(@PathVariable String studyId,
                                         @PathVariable String problemId) throws DocumentNotFoundException {
        return new ResponseEntity<>(solutionService.getSolution(studyId, problemId), HttpStatus.OK);
    }

    @PostMapping("/{studyId}/{problemId}")
    public ResponseEntity<?> saveSolution(@PathVariable String studyId,
                                          @PathVariable String problemId,
                                          @RequestBody SolutionRequestDto dto) {
        return new ResponseEntity<>(solutionService.saveOrUpdateSolution(studyId, problemId, dto), HttpStatus.OK);
    }

}