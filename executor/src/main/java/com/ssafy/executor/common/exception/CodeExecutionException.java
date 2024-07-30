package com.ssafy.executor.common.exception;

import lombok.Getter;

@Getter
public class CodeExecutionException extends RuntimeException {
    private final Long id;

    public CodeExecutionException(String message, Long id) {
        super(message);
        this.id = id;
    }
}
