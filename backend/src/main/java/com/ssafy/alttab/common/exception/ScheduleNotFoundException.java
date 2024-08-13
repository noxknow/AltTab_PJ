package com.ssafy.alttab.common.exception;

public class ScheduleNotFoundException extends RuntimeException{
    public ScheduleNotFoundException(Long studyId) {
        super(studyId +  " 스케줄을 찾을 수 없습니다.");
    }
}
