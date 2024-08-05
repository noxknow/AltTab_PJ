package com.ssafy.alttab.study.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddMembersToStudyDto {
    private Long studyId;
    private List<Long> memberIds;
}
