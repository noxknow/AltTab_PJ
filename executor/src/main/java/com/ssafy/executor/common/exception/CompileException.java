package com.ssafy.executor.common.exception;

import com.ssafy.executor.dto.CodeExecutionRequestDto;
import lombok.Getter;

@Getter
public class CompileException extends Exception {
    private final CodeExecutionRequestDto request;

    public CompileException(String message, CodeExecutionRequestDto request) {
        super(message);
        this.request = request;
    }
}
