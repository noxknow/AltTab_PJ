package com.ssafy.alttab.common.handler;

import com.ssafy.alttab.common.dto.ErrorResponseDto;
import com.ssafy.alttab.common.exception.CodeNotFoundException;
import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.ProblemNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.common.exception.TokenNotFoundException;
import com.ssafy.alttab.common.exception.TokenNotValidException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //== not found ==//
    @ExceptionHandler(CodeNotFoundException.class)
    public ResponseEntity<?> handleCodeNotFoundException(CodeNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<?> handleMemberNotFoundException(MemberNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudyNotFoundException.class)
    public ResponseEntity<?> handleStudyNotFoundException(StudyNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProblemNotFoundException.class)
    public ResponseEntity<?> handleProblemNotFoundException(ProblemNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex){
        return ErrorResponseDto.toResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<?> handleTokenNotFoundException(TokenNotFoundException ex) {
        return ErrorResponseDto.toResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    //== not valid ==//
    @ExceptionHandler(TokenNotValidException.class)
    public ResponseEntity<?> handleTokenNotValidException(TokenNotValidException ex) {
        return ErrorResponseDto.toResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    //== unExcepted ==//
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return ErrorResponseDto.toResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
