package com.ssafy.alttab.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FollowStudyListResponseDto {
    List<FollowStudyResponseDto> followStudyList;
}
