package com.ssafy.alttab.solution.controller;


import com.ssafy.alttab.solution.document.Solution;
import com.ssafy.alttab.solution.dto.SolutionRequestDto;
import com.ssafy.alttab.solution.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/solutions")
@RequiredArgsConstructor
public class SolutionController {
    private final SolutionService solutionService;

    @GetMapping("/{studyId}/{solutionId}")
    public Solution getSolution(@PathVariable String studyId,
                                @PathVariable String solutionId) {
        return solutionService.getSolution(studyId, solutionId);
    }

    @PostMapping("/{studyId}/{solutionId}")
    public Solution saveSolution(@PathVariable String studyId,
                                 @PathVariable String solutionId,
                                 @RequestBody SolutionRequestDto dto) {
        System.out.println("?????????????????");
        return solutionService.saveSolution(studyId, solutionId, dto);
    }

}