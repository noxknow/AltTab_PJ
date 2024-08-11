package com.ssafy.alttab.common.jointable.entity;

import com.ssafy.alttab.problem.entity.Problem;
import com.ssafy.alttab.study.entity.Study;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    private LocalDate deadline;
    private String presenter;
    private Long level;
    private String tag;

    public static StudyProblem createStudyProblem(Study study, Problem problem, LocalDate deadline, String presenter) {
        return StudyProblem.builder()
                .study(study)
                .problem(problem)
                .deadline(deadline)
                .presenter(presenter)
                .level(problem.getLevel())
                .tag(problem.getRepresentative())
                .build();
    }
}
