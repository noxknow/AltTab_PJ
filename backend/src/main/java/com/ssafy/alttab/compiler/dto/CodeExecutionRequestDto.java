package com.ssafy.alt_tab.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeExecutionRequestDto {
    private String input;
    private String code;
}
