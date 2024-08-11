package com.ssafy.alttab.problem.repository;

import com.ssafy.alttab.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
