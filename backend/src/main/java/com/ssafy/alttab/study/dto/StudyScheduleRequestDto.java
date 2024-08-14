package com.ssafy.alttab.study.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StudyScheduleRequestDto {
    private Long studyId;
    private Long problemId;
    private LocalDate deadline;
}
