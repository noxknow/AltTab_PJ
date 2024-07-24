package com.ssafy.compiler.common.handler;

import com.ssafy.compiler.common.exception.CompileException;
import com.ssafy.compiler.common.exception.ExecutionException;
import com.ssafy.compiler.dto.CodeExecutionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * compile error handler
     *
     * @param ex
     * @return 컴파일 오류 메시지
     */
    @ExceptionHandler(CompileException.class)
    public ResponseEntity<CodeExecutionResponseDto> handleCompileException(CompileException ex) {
        log.error("Compile error: ", ex);
        return ResponseEntity.badRequest().body(
                CodeExecutionResponseDto.builder()
                        .errorMessage("Compile error: " + ex.getMessage())
                        .build()
        );
    }

    /**
     * execution error handler
     *
     * @param ex
     * @return 실행 오류 메시지
     */
    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<CodeExecutionResponseDto> handleExecutionException(ExecutionException ex) {
        log.error("Execution error: ", ex);
        return ResponseEntity.internalServerError().body(
                CodeExecutionResponseDto.builder()
                        .errorMessage("Execution error: " + ex.getMessage())
                        .build()
        );
    }

    /**
     * etc error handler
     *
     * @param ex
     * @return 그 외 에러 메시지
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CodeExecutionResponseDto> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);
        return ResponseEntity.internalServerError().body(
                CodeExecutionResponseDto.builder()
                        .errorMessage("An unexpected error occurred: " + ex.getMessage())
                        .build()
        );
    }
}
