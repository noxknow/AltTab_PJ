package com.ssafy.alttab.common.handler;

import com.ssafy.alttab.common.dto.ErrorResponseDto;
import com.ssafy.alttab.common.exception.CodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return ErrorResponseDto.toResponse(ex);
    }
}
