package com.ssafy.alttab.study.dto;

import com.ssafy.alttab.study.entity.StudyInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyInfoResponseDto {

    private Long id;
    private String studyName;
    private String studyDescription;
    private List<String> studyEmails;

    public static StudyInfoResponseDto createStudyInfoResponse(StudyInfo studyInfo) {

        return StudyInfoResponseDto.builder()
                .studyName(studyInfo.getStudyName())
                .studyDescription(studyInfo.getStudyDescription())
                .studyEmails(studyInfo.getStudyEmails())
                .build();
    }
}
