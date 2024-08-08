package com.ssafy.alttab.common.exception;

public class RefreshTokenNotValidException extends RuntimeException{
    public RefreshTokenNotValidException(String name){
        super(name + "님의 토큰이 만료되었습니다.");
    }
}
