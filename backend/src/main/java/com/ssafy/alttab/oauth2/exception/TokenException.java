package com.ssafy.alttab.oauth2.exception;

import com.ssafy.alttab.exception.CustomException;
import com.ssafy.alttab.exception.ErrorCode;

public class TokenException extends CustomException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}