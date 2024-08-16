package com.ssafy.executor.common.aspect;

import com.ssafy.executor.common.enums.ExceptionMessage;
import com.ssafy.executor.dto.CodeExecutionRequestDto;
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
    public Object handleCompileServiceExceptions(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            log.error(ExceptionMessage.EXECUTION_FAILED.getMessage(), ex.getMessage());
            return buildErrorResponse(ex, joinPoint);
        }
    }

    private CodeExecutionResponseDto buildErrorResponse(Throwable ex, ProceedingJoinPoint joinPoint) {
        CodeExecutionRequestDto request = extractRequestFromArgs(joinPoint.getArgs());
        return CodeExecutionResponseDto.builder()
                .studyId(request.getStudyId())
                .problemId(request.getProblemId())
                .memberId(request.getMemberId())
                .errorMessage(ex.getMessage())
                .runUsername(request.getRunUsername())
                .build();
    }

    private CodeExecutionRequestDto extractRequestFromArgs(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof CodeExecutionRequestDto) {
                return (CodeExecutionRequestDto) arg;
            }
        }

        return new CodeExecutionRequestDto();
    }
}