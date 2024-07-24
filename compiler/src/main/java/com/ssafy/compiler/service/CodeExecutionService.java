package com.ssafy.compiler.service;

import com.ssafy.compiler.common.enums.LogMessage;
import com.ssafy.compiler.dto.CodeExecutionRequestDto;
import com.ssafy.compiler.dto.CodeExecutionResponseDto;
import com.ssafy.compiler.executer.CodeCompiler;
import com.ssafy.compiler.executer.CompiledCodeExecutor;
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
    public CodeExecutionResponseDto executeCode(CodeExecutionRequestDto request) throws Exception {
        codeCompiler.compileCode(request.getCode());
        log.debug(LogMessage.COMPILE_SUCCESSFUL.getMessage());

        CodeExecutionResponseDto response = codeExecutor.executeCode(request.getInput());
        log.debug(LogMessage.EXECUTION_SUCCESSFUL.getMessage(), response.getOutput());

        return response;
    }
}