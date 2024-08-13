package com.ssafy.alttab.problem.dto;

import lombok.Getter;

@Getter
public class ProblemRequestDto {
    private Long problemId;
    private String title;
    private String tag;
    private Long level;
    private String presenter;
}
