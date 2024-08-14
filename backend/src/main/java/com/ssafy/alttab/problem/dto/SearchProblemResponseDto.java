package com.ssafy.alttab.problem.dto;

import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.problem.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class SearchProblemResponseDto {

    private long problemId;
    private String title;
    private String problemIdTitle;

    public static SearchProblemResponseDto toDto(Problem problem) {
        return SearchProblemResponseDto.builder()
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .problemIdTitle(problem.getProblemIdTitle())
                .build();
    }
}
