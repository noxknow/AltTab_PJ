package com.ssafy.alttab.problem.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class AddProblemsRequestDto {
    private LocalDate deadline;
    private List<AddProblemRequestDto> problemIds;
}
