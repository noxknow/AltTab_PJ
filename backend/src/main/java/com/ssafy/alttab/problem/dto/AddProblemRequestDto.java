package com.ssafy.alttab.problem.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddProblemRequestDto {
    private Long problemId;
    private String presenter;
}
