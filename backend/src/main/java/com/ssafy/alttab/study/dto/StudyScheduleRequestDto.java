package com.ssafy.alttab.study.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class StudyScheduleRequestDto {
    private Long studyId;
    private Long problemId;
    private LocalDate deadline;
}
