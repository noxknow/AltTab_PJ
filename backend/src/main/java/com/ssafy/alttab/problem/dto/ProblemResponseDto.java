package com.ssafy.alttab.problem.dto;

import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.problem.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
public class ProblemResponseDto {
    private Long studyProblemId;
    private Long problemId;
    private String title; // 문제 제목
    private String tag; // 문제 태그
    private Long level; // 티어
    private String presenter; // 발표자

    public static ProblemResponseDto toDto(StudyProblem studyProblem) {
        Problem problem = studyProblem.getProblem();
        return ProblemResponseDto.builder()
                .studyProblemId(studyProblem.getId())
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .tag(problem.getTag())
                .level(problem.getLevel())
                .presenter(studyProblem.getPresenter())
                .build();
    }
}
