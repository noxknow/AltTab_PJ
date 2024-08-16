package com.ssafy.alttab.common.exception;

public class ProblemNotFoundException extends Exception {
    public ProblemNotFoundException(Long id) {
        super(id + " 를 찾을 수 없습니다.");
    }
}
