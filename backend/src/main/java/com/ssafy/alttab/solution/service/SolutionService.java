package com.ssafy.alttab.solution.service;

import com.ssafy.alttab.common.exception.DocumentNotFoundException;
import com.ssafy.alttab.solution.document.Solution;
import com.ssafy.alttab.solution.dto.SolutionRequestDto;
import com.ssafy.alttab.solution.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final SolutionRepository solutionRepository;

    public Solution saveOrUpdateSolution(String studyId, String problemId, SolutionRequestDto dto) {
        return solutionRepository.save(
                solutionRepository.findByStudyIdAndProblemId(studyId, problemId)
                        .map(existingSolution -> {
                            existingSolution.setBlocks(dto.getBlocks());
                            return existingSolution;
                        })
                        .orElseGet(() -> {
                            Solution newSolution = new Solution();
                            newSolution.setStudyId(studyId);
                            newSolution.setProblemId(problemId);
                            newSolution.setBlocks(dto.getBlocks());
                            return newSolution;
                        })
        );
    }

    public Solution getSolution(String studyId, String problemId) throws DocumentNotFoundException {
        return solutionRepository.findByStudyIdAndProblemId(studyId, problemId)
                .orElseThrow(DocumentNotFoundException::new);
    }
}