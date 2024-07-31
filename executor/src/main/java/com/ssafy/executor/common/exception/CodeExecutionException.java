package com.ssafy.executor.common.exception;

import com.ssafy.executor.dto.CodeExecutionRequestDto;
import lombok.Getter;

@Getter
public class CodeExecutionException extends RuntimeException {
    private final CodeExecutionRequestDto request;

    public CodeExecutionException(String message, CodeExecutionRequestDto request) {
        super(message);
        this.request = request;
    }
}
