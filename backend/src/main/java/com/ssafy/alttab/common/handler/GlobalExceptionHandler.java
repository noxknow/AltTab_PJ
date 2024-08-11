package com.ssafy.alttab.common.handler;

import com.ssafy.alttab.common.dto.ErrorResponseDto;
import com.ssafy.alttab.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //== not found ==//
    @ExceptionHandler(CodeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleCodeNotFoundException(CodeNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleMemberNotFoundException(MemberNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex);
    }

    @ExceptionHandler(StudyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleStudyNotFoundException(StudyNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex);
    }

    @ExceptionHandler(ProblemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleProblemNotFoundException(ProblemNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleTokenNotFoundException(TokenNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex);
    }

    //== not valid ==//
    @ExceptionHandler(TokenNotValidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleTokenNotValidException(TokenNotValidException ex) {
        return ErrorResponseDto.toResponse(ex);
    }

    //== unExcepted ==//
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return ErrorResponseDto.toResponse(ex);
    }
}
