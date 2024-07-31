package com.ssafy.executor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeExecutionResponseDto {
    private Long studyGroupId;
    private Long problemId;
    private Long problemTab;
    private String output;
    private String errorMessage;
}
