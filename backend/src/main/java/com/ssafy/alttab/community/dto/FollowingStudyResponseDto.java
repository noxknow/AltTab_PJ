package com.ssafy.alttab.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FollowingStudyResponseDto {
    private Long studyId;
    private String studyName;
    private String studyDescription;
    private Long studyFollowerCount;
    private Long like;
    private Long totalFollower;
    private LeaderMemberDto leaderMemberDto;
    private boolean check;
}
