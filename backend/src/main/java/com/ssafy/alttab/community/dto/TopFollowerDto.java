package com.ssafy.alttab.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TopFollowerDto {
    private String name;
    private Long like;
    private Long totalFollower;
    private Long view;
}
