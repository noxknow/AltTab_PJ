package com.ssafy.executor.common.exception;

import lombok.Getter;

@Getter
public class CompileException extends Exception {
    private final Long id;

    public CompileException(String message, Long id) {
        super(message);
        this.id = id;
    }
}
