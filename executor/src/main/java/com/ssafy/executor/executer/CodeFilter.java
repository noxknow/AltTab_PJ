package com.ssafy.executor.executer;

import com.ssafy.executor.common.enums.ForbiddenPattern;
import java.util.ArrayList;
import java.util.List;

public class CodeFilter {

    /**
     * 소스 코드가 안전한지 확인
     *
     * @param sourceCode 검사할 소스 코드
     * @return 소스 코드가 안전하면 true, 그렇지 않으면 false
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
     * 소스 코드에서 위반된 패턴 목록 반환
     *
     * @param sourceCode 검사할 소스 코드
     * @return 위반된 패턴에 대한 설명 목록
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
