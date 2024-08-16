package com.ssafy.alttab.study.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyInfoRequestDto {
    private String studyName;
    private String studyDescription;
}

