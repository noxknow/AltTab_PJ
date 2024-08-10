package com.ssafy.alttab.common.enums;

import lombok.Getter;

@Getter
public enum FailMessage {

    // 인증 관련 메시지
    LOGIN_FAIL("로그인에 성공했습니다."),
    LOGOUT_FAIL("로그아웃에 성공했습니다."),
    ACCESS_TOKEN_NOT_FOUND("access token을 찾을 수 없습니다."),
    ACCESS_TOKEN_NOT_VALID("유효하지 않은 access token입니다."),
    REFRESH_TOKEN_NOT_FOUND("refresh token을 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_VALID("유효하지 않은 refresh token입니다.");

    private final String message;

    FailMessage(String message) {
        this.message = message;
    }

}
