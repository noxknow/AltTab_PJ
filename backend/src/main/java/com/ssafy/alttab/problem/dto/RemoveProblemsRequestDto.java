package com.ssafy.alttab.problem.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RemoveProblemsRequestDto {
    List<Long> problemStudyIds;
}
