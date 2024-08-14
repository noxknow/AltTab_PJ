package com.ssafy.alttab.problem.repository;

import com.ssafy.alttab.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByProblemIdTitleContaining(String problemInfo);
}
