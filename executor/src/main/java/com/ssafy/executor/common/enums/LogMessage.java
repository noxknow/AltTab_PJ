package com.ssafy.executor.common.enums;

import lombok.Getter;

@Getter
public enum LogMessage {
    // 컴파일
    COMPILE_STARTED("Compile started for code: {}"),
    COMPILE_SUCCESSFUL("Compile successful"),
    COMPILE_FAILED("Compile failed: {}"),

    // 코드 실행
    EXECUTION_STARTED("Execution started with input: {}"),
    EXECUTION_SUCCESSFUL("Execution successful"),
    EXECUTION_FAILED("Execution failed: {}");

    private final String message;

    LogMessage(String message) {
        this.message = message;
    }
}
