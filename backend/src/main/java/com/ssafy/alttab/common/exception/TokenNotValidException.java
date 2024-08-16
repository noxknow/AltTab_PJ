package com.ssafy.alttab.common.exception;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException(String name) {
        super(name + "님의 토큰이 만료되었습니다.");
    }
}
