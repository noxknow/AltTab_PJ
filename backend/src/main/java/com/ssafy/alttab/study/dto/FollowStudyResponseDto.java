package com.ssafy.alttab.study.dto;

import com.ssafy.alttab.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FollowStudyResponseDto {
    private Long studyId;
    private String studyName;
    private String studyDescription;
    public Long studyFollowerCount;

    public static FollowStudyResponseDto toDto(Study study) {
        return FollowStudyResponseDto.builder()
                .studyId(study.getId())
                .studyName(study.getStudyName())
                .studyDescription(study.getStudyDescription())
                .studyFollowerCount(study.getFollowerCount())
                .build();
    }
}
