package com.ssafy.alttab.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopSolverDto {
    private String name;
    private Long studyId;
    private Long like;
    private Long totalSolve;
    private Long view;
}