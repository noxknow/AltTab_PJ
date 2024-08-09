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
    private String name;
    private Long studyId;
    private Long like;
    private Long follower;
    private Long view;
}