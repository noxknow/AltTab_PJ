package com.ssafy.executor.service;

import com.ssafy.executor.common.enums.LogMessage;
import com.ssafy.executor.dto.CodeExecutionRequestDto;
import com.ssafy.executor.dto.CodeExecutionResponseDto;
import com.ssafy.executor.executer.CodeCompiler;
import com.ssafy.executor.executer.CompiledCodeExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeExecutionService {

    private final CodeCompiler codeCompiler;
    private final CompiledCodeExecutor codeExecutor;

    /**
     * 전체적인 코드 실행
     *
     * @param request
     * @return output, errorMessage
     * @throws Exception
     */
    @RabbitListener(queues = "code-execution-request-queue")
    @SendTo("code-execution-response-queue")
    public CodeExecutionResponseDto executeCode(CodeExecutionRequestDto request) throws Throwable {
        codeCompiler.compileCode(request.getCode());
        log.debug(LogMessage.COMPILE_SUCCESSFUL.getMessage());

        CodeExecutionResponseDto response = codeExecutor.executeCode(request.getCode(), request.getInput());
        log.debug(LogMessage.EXECUTION_SUCCESSFUL.getMessage(), response.getOutput());

        return response;
    }
}