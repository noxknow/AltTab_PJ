package com.ssafy.alttab.problem.dto;

import com.ssafy.alttab.problem.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchProblemResponseDto {

    private Long problemId;
    private String title;

    public static SearchProblemResponseDto toDto(Problem problem) {
        return SearchProblemResponseDto.builder()
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .build();
    }
}
