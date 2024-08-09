package com.ssafy.executor.executer;

import com.ssafy.executor.common.enums.ForbiddenPattern;
import java.util.ArrayList;
import java.util.List;

public class CodeFilter {

    /**
     * 금지 패턴 탐색
     *
     * @param sourceCode
     * @return 금지 패턴 일치 여부
     */
    public static boolean isSafeCode(String sourceCode) {
        for (ForbiddenPattern pattern : ForbiddenPattern.values()) {
            if (pattern.getPattern().matcher(sourceCode).find()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 금지 패턴 목록 조회
     *
     * @param sourceCode
     * @return 금지 패턴 목록
     */
    public static List<String> getViolations(String sourceCode) {
        List<String> violations = new ArrayList<>();
        for (ForbiddenPattern pattern : ForbiddenPattern.values()) {
            if (pattern.getPattern().matcher(sourceCode).find()) {
                violations.add(pattern.getDescription());
            }
        }
        return violations;
    }
}
