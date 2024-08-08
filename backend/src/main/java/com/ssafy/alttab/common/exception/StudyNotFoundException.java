package com.ssafy.alttab.common.exception;

public class StudyNotFoundException extends Exception {
    public StudyNotFoundException(Long studyId) {
        super(studyId + " 스터디 를 찾을 수 없습니다.");
    }
}
