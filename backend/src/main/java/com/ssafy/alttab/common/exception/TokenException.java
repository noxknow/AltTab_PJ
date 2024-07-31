package com.ssafy.alttab.common.exception;

import com.ssafy.alttab.common.enums.ErrorCode;

public class TokenException extends CustomException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}