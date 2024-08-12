package com.ssafy.alttab.community.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopFollowerDto {
    private Long studyId;
    private String name;
    private String studyDescription;
    private Long like;
    private Long totalFollower;
    private Long view;
    private LeaderMemberDto leaderMemberDto;
    private boolean check;
}
