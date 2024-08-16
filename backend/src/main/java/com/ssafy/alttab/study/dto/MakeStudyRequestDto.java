package com.ssafy.alttab.study.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MakeStudyRequestDto {
    private String studyName;
    private String studyDescription;
    private List<Long> memberIds;
}
