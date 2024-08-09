package com.ssafy.alttab.study.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyInfoResponseDto {
    private String studyName;
    private String studyDescription;
}
