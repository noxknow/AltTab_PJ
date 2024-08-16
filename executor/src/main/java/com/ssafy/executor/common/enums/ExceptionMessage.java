package com.ssafy.executor.common.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    // 코드 필터 오류
    UNSAFE_CODE_DETECTED("Unsafe code detected: {}"),

    // 컴파일 오류
    COMPILATION_FAILED("Compilation failed: {}"),
    COMPILATION_TIMEOUT("Compilation timed out after {} seconds"),

    // 실행 오류
    EXECUTION_FAILED("Execution failed: {}"),
    EXECUTION_TIMEOUT("Execution timed out after {} seconds"),
    EXECUTION_INTERRUPTED("Code execution was interrupted"),
    EXECUTION_FAILED_WITH_EXIT_CODE("Execution failed with exit code: {}"),

    // 그 외 오류
    UNEXPECTED_ERROR("An unexpected error occurred: {}"),
    STACK_TRACE_WRITE_FAILED("Failed to write stack trace to error stream");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String formatMessage(Object... args) {
        return String.format(this.message.replace("{}", "%s"), args);
    }
}