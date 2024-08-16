package com.ssafy.alttab.study.dto;

import com.ssafy.alttab.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StudyInfoResponseDto {
    private Long studyId;
    private String studyName;
    private String studyDescription;

    public static StudyInfoResponseDto toDto(Study study) {
        return StudyInfoResponseDto.builder()
                .studyId(study.getId())
                .studyName(study.getStudyName())
                .studyDescription(study.getStudyDescription())
                .build();
    }
}
