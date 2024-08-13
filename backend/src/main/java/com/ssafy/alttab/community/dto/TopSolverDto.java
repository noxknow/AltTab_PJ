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
    private Long studyId;
    private String studyName;
    private String studyDescription;
    private Long like;
    private Long totalSolve;
    private Long view;
    private LeaderMemberDto leaderMemberDto;
    private boolean check;
}