package com.ssafy.alttab.common.jointable.entity;

import com.ssafy.alttab.problem.entity.Problem;
import com.ssafy.alttab.study.entity.StudySchedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ScheduleProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_schedule_id")
    private StudySchedule studySchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    private String presenter;

    //== 생성 메서드 ==//
    public static ScheduleProblem createStudySchedule(StudySchedule studySchedule, Problem problem, String presenter) {
        ScheduleProblem scheduleProblem = ScheduleProblem.builder()
                .studySchedule(studySchedule)
                .problem(problem)
                .presenter(presenter)
                .build();
        studySchedule.getScheduleProblems().add(scheduleProblem);
        return scheduleProblem;
    }

    //== 비즈니스 로직 ==//
    public void changeStudySchedule(StudySchedule studySchedule) {
        this.studySchedule = studySchedule;
    }
}
