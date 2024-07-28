package com.ssafy.executor.common.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    UNSAFE_CODE_DETECTED("Unsafe code detected: %s"),
    COMPILATION_FAILED("Compilation failed: %s"),
    COMPILATION_TIMEOUT("Compilation timed out after %d seconds"),
    EXECUTION_FAILED("Execution failed: %s"),
    EXECUTION_TIMEOUT("Execution timed out after %d seconds"),
    EXECUTION_INTERRUPTED("Code execution was interrupted"),
    UNEXPECTED_ERROR("An unexpected error occurred: %s"),
    STACK_TRACE_WRITE_FAILED("Failed to write stack trace to error stream"),
    EXECUTION_FAILED_WITH_EXIT_CODE("Execution failed with exit code: %d");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}