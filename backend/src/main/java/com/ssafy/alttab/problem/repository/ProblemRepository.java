package com.ssafy.alttab.problem.repository;

import com.ssafy.alttab.problem.entity.Problem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query("SELECT p FROM Problem p WHERE " +
            "CAST(p.problemId AS string) LIKE :problemInfo% OR " +
            "p.problemIdTitle LIKE %:problemInfo% " +
            "ORDER BY CASE WHEN CAST(p.problemId AS string) LIKE :problemInfo% THEN 0 ELSE 1 END, " +
            "p.problemId")
    Page<Problem> findByProblemIdTitleContainingOrdered(@Param("problemInfo") String problemInfo, Pageable pageable);
}