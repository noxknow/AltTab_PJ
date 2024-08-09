package com.ssafy.alttab.community.dto;

import com.ssafy.alttab.study.entity.Study;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopFollowerDto {
    private String name;
    private Long like;
    private Long totalFollower;
    private Long view;

    public static TopFollowerDto toDto(Study study) {
        return TopFollowerDto.builder()
                .name(study.getStudyName())
                .like(study.getLike())
                .totalFollower(study.getFollowerCount())  // 팔로워 수로 변경
                .view(study.getView())
                .build();
    }
}
