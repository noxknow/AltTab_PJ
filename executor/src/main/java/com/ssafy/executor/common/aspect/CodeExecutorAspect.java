package com.ssafy.executor.common.aspect;

import com.ssafy.executor.common.exception.CompileException;
import com.ssafy.executor.dto.CodeExecutionResponseDto;
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

    @Around("execution(* com.ssafy.executor.service.CodeExecutionService.executeCode(..))")
    public Object handleCompileServiceExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (CompileException ex) {
            log.error("Compile error: ", ex);
            return CodeExecutionResponseDto.builder()
                    .errorMessage("Compile error: " + ex.getMessage())
                    .build();
        } catch (SecurityException ex){
            log.error("Security error: ", ex);
            return CodeExecutionResponseDto.builder()
                    .errorMessage("Security error: " + ex.getMessage())
                    .build();
        } catch (TimeoutException ex){
            log.error("Timeout error: ", ex);
            return CodeExecutionResponseDto.builder()
                    .errorMessage("Timeout error: " + ex.getMessage())
                    .build();
        }
        catch (Throwable ex) {
            log.error("Unexpected error: ", ex);
            return CodeExecutionResponseDto.builder()
                    .errorMessage("Unexpected error: " + ex.getMessage())
                    .build();
        }
    }
}
