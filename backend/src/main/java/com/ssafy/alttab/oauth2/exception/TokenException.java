package com.ssafy.alt_tab.oauth2.exception;

import com.ssafy.alt_tab.exception.CustomException;
import com.ssafy.alt_tab.exception.ErrorCode;

public class TokenException extends CustomException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}