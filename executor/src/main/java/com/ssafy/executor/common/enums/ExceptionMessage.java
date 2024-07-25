package com.ssafy.executor.common.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    UNSAFE_CODE_DETECTED("Unsafe code detected: %s"),
    COMPILATION_FAILED("Compilation failed: %s"),
    EXECUTION_FAILED("Execution failed: %s"),
    EXECUTION_TIMEOUT("Execution timed out after %d ms"),
    EXECUTION_INTERRUPTED("Code execution was interrupted"),
    UNEXPECTED_ERROR("An unexpected error occurred: %s"),
    STACK_TRACE_WRITE_FAILED("Failed to write stack trace to error stream");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
