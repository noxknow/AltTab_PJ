package com.ssafy.alt_tab.compiler.service;

import com.ssafy.alt_tab.compiler.dto.CodeExecutionRequestDto;
import com.ssafy.alt_tab.compiler.dto.CodeExecutionResponseDto;
import com.ssafy.alt_tab.compiler.entity.CodeSnippet;
import com.ssafy.alt_tab.compiler.repository.CodeSnippetRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final RabbitTemplate rabbitTemplate;
    private final CodeSnippetRepository codeSnippetRepository;
    private CompletableFuture<CodeExecutionResponseDto> futureResponse;

    public Long saveCode(String code){
        CodeSnippet codeSnippet = CodeSnippet.builder().
                code(code)
                .build();

        return codeSnippetRepository.save(codeSnippet).getId();
    }


    public CompletableFuture<CodeExecutionResponseDto> executeCode(CodeExecutionRequestDto request) {
        futureResponse = new CompletableFuture<>();
        rabbitTemplate.convertAndSend("code-execution-request-queue", request);
        return futureResponse;
    }

    @RabbitListener(queues = "code-execution-response-queue")
    public void receive(CodeExecutionResponseDto response){
        futureResponse.complete(response);
    }
}
