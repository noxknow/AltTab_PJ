package com.ssafy.compiler.common.enums;

import lombok.Getter;

@Getter
public enum LogMessage {
    COMPILE_STARTED("Compile started for code: {}"),
    COMPILE_SUCCESSFUL("Compile successful"),
    COMPILE_FAILED("Compile failed: {}"),
    EXECUTION_STARTED("Execution started with input: {}"),
    EXECUTION_SUCCESSFUL("Execution successful. Output: {}"),
    EXECUTION_FAILED("Execution failed: {}");

    private final String message;

    LogMessage(String message) {
        this.message = message;
    }
}
