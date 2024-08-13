package com.ssafy.alttab.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FollowingStudyDto {
    private Long studyId;
    private String studyName;
    private String studyDescription;
    private Long like;
    private Long totalSolve;
    private LeaderMemberDto leaderMemberDto;
    private boolean check;
}
