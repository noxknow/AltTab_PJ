package com.ssafy.alttab.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeExecutionRequestDto {
    private Long studyGroupId;
    private Long problemId;
    private Long problemTab;
    private String input;
    private String code;
}
