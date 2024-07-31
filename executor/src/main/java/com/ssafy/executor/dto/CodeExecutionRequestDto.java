package com.ssafy.executor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeExecutionRequestDto {
    private Long studyGroupId;
    private Long problemId;
    private Long problemTab;
    private String input;
    private String code;
}
