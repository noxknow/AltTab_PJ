package com.ssafy.alttab.community.dto;

import com.ssafy.alttab.study.entity.StudyInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopFollowerDto {
    private String name;
    private Long like;
    private Long totalFollower;
    private Long view;

    public static TopFollowerDto toDto(StudyInfo studyInfo){
        return TopFollowerDto.builder()
                .name(studyInfo.getStudyName())
                .like(studyInfo.getLike())
                .totalFollower(studyInfo.getFollowerCount())  // 팔로워 수로 변경
                .view(studyInfo.getView())
                .build();
    }
}
