package com.ssafy.alttab.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AddMembersRequestDto {
    private Long studyId;
    private List<Long> memberIds;
}
