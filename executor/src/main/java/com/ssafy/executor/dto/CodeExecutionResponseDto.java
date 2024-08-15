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
    private Long studyId;
    private Long problemId;
    private Long memberId;
    private String output;
    private String errorMessage;
    private String runUsername;
}
