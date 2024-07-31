package com.ssafy.alttab.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyInfoRequestDto {

    private Long studyId;
    private String studyName;
    private List<String> studyEmails;
    private String studyInfo;
}
