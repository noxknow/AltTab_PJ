package com.ssafy.alttab.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LeaderMemberDto {
    private String name;
    private String avatarUrl;
}
