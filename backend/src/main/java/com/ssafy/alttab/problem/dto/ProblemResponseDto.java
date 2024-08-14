package com.ssafy.alttab.problem.dto;

import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.problem.entity.Problem;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ProblemResponseDto {
    private Long studyProblemId;
    private Long problemId;
    private String title;
    private String tag;
    private Long level;
    private String presenter;
    private LocalDate deadline;

    public static ProblemResponseDto toDto(StudyProblem studyProblem) {
        Problem problem = studyProblem.getProblem();
        return ProblemResponseDto.builder()
                .studyProblemId(studyProblem.getId())
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .tag(problem.getTag())
                .level(problem.getLevel())
                .presenter(studyProblem.getPresenter())
                .deadline(studyProblem.getDeadline())
                .build();
    }
}
