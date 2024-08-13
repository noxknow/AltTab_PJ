package com.ssafy.alttab.study.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StudyScheduleRequestDto {
    private Long studyId;
    private LocalDate deadline;
    private String presenter;
    private List<Long> problemId;
}
