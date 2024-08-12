package com.ssafy.alttab.problem.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ProblemRequestDto {
    private Long problemId;
    private String title;
    private String tag;
    private Long level;
    private String presenter;
    private LocalDate deadline;
}
