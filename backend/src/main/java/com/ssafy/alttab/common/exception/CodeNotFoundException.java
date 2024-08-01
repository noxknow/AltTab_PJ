package com.ssafy.alttab.common.exception;

public class CodeNotFoundException extends RuntimeException{

    public CodeNotFoundException(Long studyGroupId, Long problemId, Long problemTab) {
        super(String.format("Code not found for studyGroupId: %d, problemId: %d, problemTab: %d",
                studyGroupId, problemId, problemTab));
    }
}
