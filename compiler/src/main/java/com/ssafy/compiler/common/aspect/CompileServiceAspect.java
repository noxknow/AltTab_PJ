package com.ssafy.compiler.common.aspect;

import com.ssafy.compiler.common.exception.CompileException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CompileServiceAspect {

    @Around("execution(* com.ssafy.compiler.service.CodeExecutionService.executeCode(..))")
    public Object handleCompileServiceExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (CompileException ex) {
            log.error("Compile error: ", ex);
            throw new CompileException(ex.getMessage());
        } catch (Throwable ex) {
            log.error("Unexpected error: ", ex);
            throw new Exception(ex.getMessage());
        }
    }
}
