package com.ssafy.executor.service;

import com.ssafy.executor.dto.CodeExecutionRequestDto;
import com.ssafy.executor.dto.CodeExecutionResponseDto;
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

    private final CompiledCodeExecutor codeExecutor;

    /**
     * RabbitMQ를 통해 받은 코드 실행 요청을 처리하고 결과를 반환
     *
     * @param request 코드 실행 요청 정보를 담은 DTO
     * @return 코드 실행 결과를 담은 CodeExecutionResponseDto
     * @throws Throwable 코드 실행 중 발생할 수 있는 다양한 예외
     */
    @RabbitListener(queues = "code-execution-request-queue")
    @SendTo("code-execution-response-queue")
    public CodeExecutionResponseDto executeCode(CodeExecutionRequestDto request) throws Throwable {
        return codeExecutor.executeCode(request);
    }
}