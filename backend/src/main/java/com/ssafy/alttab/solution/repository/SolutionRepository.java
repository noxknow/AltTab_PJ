package com.ssafy.alttab.solution.repository;

import com.ssafy.alttab.solution.document.Solution;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionRepository extends MongoRepository<Solution, ObjectId> {
    Optional<Solution> findByStudyIdAndProblemId(String studyId, String problemId);
}