package com.ssafy.alttab.study.dto;

import com.ssafy.alttab.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class StudyScoreResponseDto {
    private int[] tagCount;
    private Long totalScore;
    private Long solveCount;
    private int rank;

    public static StudyScoreResponseDto toDto(Study study, int[] tagCount, int rank) {
        return StudyScoreResponseDto.builder()
                .tagCount(tagCount)
                .totalScore(study.getStudyPoint())
                .solveCount(study.getSolveCount())
                .rank(rank)
                .build();
    }
}
