package com.ssafy.alttab.study.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JoinedStudyResponseDto {
    private Long studyId;
    private String studyName;
}
