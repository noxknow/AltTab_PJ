package com.ssafy.alttab.problem.repository;

import com.ssafy.alttab.problem.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findByStudyIdAndProblemId(Long studyId, Long problemId);
}
