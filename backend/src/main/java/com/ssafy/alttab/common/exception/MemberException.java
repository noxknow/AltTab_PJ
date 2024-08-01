package com.ssafy.alttab.common.exception;

import com.ssafy.alttab.common.enums.ErrorCode;

public class MemberException extends CustomException {

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}