package com.ssafy.alttab.common.dto;

import com.ssafy.alttab.common.exception.CodeNotFoundException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ErrorResponseDto {

    private final String message;

    /**
     * 예외 객체로부터 에러 메시지를 추출하여 초기화하는 생성자
     *
     * @param ex 예외 객체
     */
    public ErrorResponseDto(Exception ex) {
        this.message = ex.getMessage();
    }

    /**
     * 예외와 HTTP 상태 코드를 기반으로 ResponseEntity 를 생성하는 메소드
     *
     * @param ex 예외 객체
     * @param httpStatus 반환할 HTTP 상태 코드
     * @return 에러 응답 DTO 와 HTTP 상태 코드가 포함된 ResponseEntity 객체
     */
    public static ResponseEntity<?> toResponse(Exception ex, HttpStatus httpStatus) {
        ErrorResponseDto response = new ErrorResponseDto(ex);
        return new ResponseEntity<>(response, httpStatus);
    }
}
