package com.ssafy.alttab.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProblemListResponseDto {
    List<ProblemResponseDto> problemList;
}
