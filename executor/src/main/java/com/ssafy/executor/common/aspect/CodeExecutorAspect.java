package com.ssafy.executor.common.aspect;

import com.ssafy.executor.common.enums.ExceptionMessage;
import com.ssafy.executor.dto.CodeExecutionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CodeExecutorAspect {
    @Around("execution(* com.ssafy.executor.service.CodeExecutionService.executeCode(..))")
    public Object handleCompileServiceExceptions(ProceedingJoinPoint joinPoint){
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            log.error(ExceptionMessage.EXECUTION_FAILED.getMessage(), ex.getMessage());
            return buildErrorResponse(ex);
        }
    }

    private CodeExecutionResponseDto buildErrorResponse(Throwable ex) {
        return CodeExecutionResponseDto.builder()
                .errorMessage(ex.getMessage())
                .build();
    }
}