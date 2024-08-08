package com.ssafy.alttab.common.handler;

import com.ssafy.alttab.common.dto.ErrorResponseDto;
import com.ssafy.alttab.common.exception.CodeNotFoundException;
import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.RefreshTokenNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CodeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleCodeNotFoundException(CodeNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleMemberNotFoundException(Exception ex) {
        return ErrorResponseDto.toResponse(ex);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex);
    }

    @ExceptionHandler(RefreshTokenNotValidException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<?> handleRefreshTokenNotValidException(RefreshTokenNotValidException ex) {
        return ErrorResponseDto.toResponse(ex);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return ErrorResponseDto.toResponse(ex);
    }
}
