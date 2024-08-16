package com.ssafy.alttab.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    // 인증 관련 메시지
    LOGIN_SUCCESS("로그인에 성공했습니다."),
    LOGOUT_SUCCESS("로그아웃에 성공했습니다."),
    ACCESS_TOKEN_VALID("유효한 access token입니다");

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
