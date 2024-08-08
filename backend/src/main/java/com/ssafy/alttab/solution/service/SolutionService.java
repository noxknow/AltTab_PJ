package com.ssafy.alttab.solution.service;

import com.ssafy.alttab.solution.document.Solution;
import com.ssafy.alttab.solution.dto.SolutionRequestDto;
import com.ssafy.alttab.solution.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SolutionService {
    //    private final SolutionRepository solutionRepository;
//
//    public Solution saveSolution(String studyId, String problemId, SolutionRequestDto dto) {
//        Solution solution = solutionRepository.findByStudyIdAndProblemId(studyId, problemId);
//        if (solution == null) {
//            solution = new Solution(new ObjectId(), studyId, problemId, dto.getBlocks());
//        } else {
//            solution.setBlocks(dto.getBlocks());
//        }
//        return solutionRepository.save(solution);
//    }
//
//    public Solution getSolution(String studyId, String problemId) {
//        return solutionRepository.findByStudyIdAndProblemId(studyId, problemId);
//    }
    private final SolutionRepository solutionRepository;

    public Solution saveSolution(String studyId, String problemId, SolutionRequestDto dto) {
        Optional<Solution> existingSolution = solutionRepository.findByStudyIdAndProblemId(studyId, problemId);
        if (existingSolution.isPresent()) {
            // Update existing solution
            Solution solution = existingSolution.get();
            solution.setBlocks(dto.getBlocks());
            return solutionRepository.save(solution);
        } else {
            // Create new solution
            Solution newSolution = new Solution();
            newSolution.setStudyId(studyId);
            newSolution.setProblemId(problemId);
            newSolution.setBlocks(dto.getBlocks());
            return solutionRepository.save(newSolution);
        }
    }

    public Solution getSolution(String studyId, String problemId) {
        Optional<Solution> existingSolution = solutionRepository.findByStudyIdAndProblemId(studyId, problemId);
        return existingSolution.get();
    }
}