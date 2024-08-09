package com.ssafy.alttab.common.exception;


public class TokenNotFoundException extends RuntimeException{

    public TokenNotFoundException(String token){
        super(token + "을 찾을 수 없습니다.");
    }
}
