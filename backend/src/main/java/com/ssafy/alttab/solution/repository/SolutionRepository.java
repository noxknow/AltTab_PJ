package com.ssafy.alttab.solution.repository;

import com.ssafy.alttab.solution.document.Solution;
import com.ssafy.alttab.solution.document.SolutionId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface SolutionRepository extends MongoRepository<Solution, ObjectId> {
//    Solution findByStudyIdAndProblemId(String studyId, String problemId);

//    Optional<Solution> findById(SolutionId id);

    Optional<Solution> findByStudyIdAndProblemId(String studyId, String problemId);
}