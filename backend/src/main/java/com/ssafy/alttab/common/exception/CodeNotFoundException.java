package com.ssafy.alttab.common.exception;

public class CodeNotFoundException extends RuntimeException {

    public CodeNotFoundException(Long studyId, Long problemId, Long memberId) {
        super(String.format("Code not found for studyId: %d, problemId: %d, memberId: %d",
                studyId, problemId, memberId));
    }
}
