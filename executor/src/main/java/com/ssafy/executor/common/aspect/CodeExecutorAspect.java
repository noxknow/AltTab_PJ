package com.ssafy.executor.common.aspect;

import com.ssafy.executor.common.enums.ExceptionMessage;
import com.ssafy.executor.common.exception.CompileException;
import com.ssafy.executor.dto.CodeExecutionResponseDto;

import java.util.Map;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CodeExecutorAspect {

    /**
     * 예외가 있을 때 추가
     */
    private static final Map<Class<? extends Throwable>, ExceptionMessage> EXCEPTION_MAPPINGS = Map.of(
            CompileException.class, ExceptionMessage.COMPILATION_FAILED,
            SecurityException.class, ExceptionMessage.UNSAFE_CODE_DETECTED,
            TimeoutException.class, ExceptionMessage.EXECUTION_TIMEOUT,
            InterruptedException.class, ExceptionMessage.EXECUTION_INTERRUPTED
    );

    @Around("execution(* com.ssafy.executor.service.CodeExecutionService.executeCode(..))")
    public Object handleCompileServiceExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            log.error("Error during code execution: ", ex);
            return buildErrorResponse(ex);
        }
    }

    private CodeExecutionResponseDto buildErrorResponse(Throwable ex) {
        ExceptionMessage exceptionMessage = EXCEPTION_MAPPINGS.getOrDefault(ex.getClass(), ExceptionMessage.UNEXPECTED_ERROR);
        String errorMessage = exceptionMessage.formatMessage(ex.getMessage());

        return CodeExecutionResponseDto.builder()
                .errorMessage(errorMessage)
                .build();
    }
}