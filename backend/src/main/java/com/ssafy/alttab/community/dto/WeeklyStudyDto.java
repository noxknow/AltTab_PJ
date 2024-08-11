package com.ssafy.alttab.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyStudyDto {
    private Long studyId;
    private String name;
    private String studyDescription;
    private Long like;
    private Long follower;
    private Long view;
    private LeaderMemberDto leaderMemberDto;
}