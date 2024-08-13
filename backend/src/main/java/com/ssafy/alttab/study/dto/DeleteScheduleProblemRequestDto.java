package com.ssafy.alttab.study.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class DeleteScheduleProblemRequestDto {
    private Long studyId;
    private LocalDate deadline;
    private Long problemId;
}
