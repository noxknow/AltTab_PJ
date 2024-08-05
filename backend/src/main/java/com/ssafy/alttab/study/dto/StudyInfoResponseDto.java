package com.ssafy.alttab.study.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyInfoResponseDto {
    private Long studyId;
    private Long id;
    private String studyName;
    private List<String> studyEmails;
    private String studyDescription;
}
