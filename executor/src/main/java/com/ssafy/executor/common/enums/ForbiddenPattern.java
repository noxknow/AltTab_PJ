package com.ssafy.executor.common.enums;

import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public enum ForbiddenPattern {
    DATABASE("java\\.sql|jdbc", "Database access"),
    FILE_SYSTEM("java\\.io\\.File|java\\.nio\\.file", "File system access"),
    NETWORK("java\\.net", "Network operations"),
    SYSTEM_EXEC("Runtime\\.getRuntime\\(\\)\\.exec|ProcessBuilder", "System command execution"),
    REFLECTION("java\\.lang\\.reflect", "Reflection"),
    THREAD("java\\.lang\\.Thread", "Thread creation");

    private final Pattern pattern;
    private final String description;

    ForbiddenPattern(String regex, String description) {
        this.pattern = Pattern.compile(regex);
        this.description = description;
    }
}
