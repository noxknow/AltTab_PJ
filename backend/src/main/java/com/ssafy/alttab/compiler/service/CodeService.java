package com.ssafy.alttab.compiler.service;

import com.ssafy.alttab.compiler.dto.CodeExecutionRequestDto;
import com.ssafy.alttab.compiler.dto.CodeExecutionResponseDto;
import com.ssafy.alttab.compiler.entity.CodeSnippet;
import com.ssafy.alttab.compiler.enums.ExecutionStatus;
import com.ssafy.alttab.compiler.repository.CodeSnippetRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
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

    /**
     * 코드와 실행 ID를 저장하거나 업데이트합
     *
     * @param code 저장할 코드 내용
     * @param id 코드 스니펫의 ID
     * @return 저장된 코드 스니펫의 ID
     */
    @Transactional
    public Long saveCode(String code, Long id) {
        CodeSnippet codeSnippet = updateCodeIfExisting(code, id);
        return codeSnippetRepository.save(codeSnippet).getId();
    }

    /**
     * 주어진 ID에 해당하는 코드 스니펫이 존재하면 업데이트하고, 없으면 새로 생성
     *
     * @param code 업데이트하거나 새로 생성할 코드 내용
     * @param id 코드 스니펫의 ID
     * @return 업데이트되거나 새로 생성된 CodeSnippet 객체
     */
    public CodeSnippet updateCodeIfExisting(String code, Long id) {
        return codeSnippetRepository.findById(id)
                .map(snippet -> updateExistingCodeSnippet(snippet, code))
                .orElseGet(() -> createNewCodeSnippet(code));
    }

    /**
     * 기존 코드 스니펫을 새로운 코드로 업데이트
     *
     * @param snippet 업데이트할 코드 스니펫 객체
     * @param newCode 새로운 코드 내용
     * @return 업데이트된 CodeSnippet 객체
     */
    private CodeSnippet updateExistingCodeSnippet(CodeSnippet snippet, String newCode) {
        snippet.changeCode(newCode);
        snippet.changeExecutionStatus(ExecutionStatus.NOT_START);
        return snippet;
    }

    /**
     * 새로운 코드 스니펫을 생성
     *
     * @param code 새로운 코드 내용
     * @return 생성된 CodeSnippet 객체
     */
    private CodeSnippet createNewCodeSnippet(String code) {
        return CodeSnippet.builder()
                .code(code)
                .executionStatus(ExecutionStatus.NOT_START)
                .build();
    }

    /**
     * 코드 실행을 비동기적으로 요청하고 초기 응답을 반환
     *
     * @param request 코드 실행 요청 정보를 담은 DTO
     * @param id 코드 스니펫의 ID
     * @return 초기 실행 상태를 담은 CodeExecutionResponseDto 객체
     */
    public CodeExecutionResponseDto executeCodeAsync(CodeExecutionRequestDto request, Long id) {
        CodeExecutionResponseDto response = CodeExecutionResponseDto.builder()
                .id(id)
                .status(ExecutionStatus.IN_PROGRESS)
                .build();

        CompletableFuture.runAsync(() -> {
            rabbitTemplate.convertAndSend("code-execution-request-queue", request);
        });

        return response;
    }

    /**
     * 코드 실행 결과를 받아 처리
     *
     * @param response 코드 실행 결과를 담은 DTO
     */
    @RabbitListener(queues = "code-execution-response-queue")
    public void receive(CodeExecutionResponseDto response) {
        Long id = response.getId();
        Optional<CodeSnippet> codeSnippet = codeSnippetRepository.findById(id);

        if (codeSnippet.isPresent()) {
            CodeSnippet update = codeSnippet.get();
            update.changeExecutionStatus(ExecutionStatus.DONE);
            codeSnippetRepository.save(update);
        }
    }

    /**
     * 주어진 ID에 해당하는 코드 실행 결과를 조회
     *
     * @param id 조회할 코드 스니펫의 ID
     * @return 코드 실행 결과를 담은 CodeExecutionResponseDto 객체
     */
    public CodeExecutionResponseDto getExecutionResult(Long id) {
        Optional<CodeSnippet> codeSnippet = codeSnippetRepository.findById(id);
        return codeSnippet.map(this::mapToResponseDto)
                .orElseGet(() -> createFailResponseDto(id));
    }

    /**
     * CodeSnippet 객체를 CodeExecutionResponseDto 객체로 변환
     *
     * @param snippet 변환할 CodeSnippet 객체
     * @return 변환된 CodeExecutionResponseDto 객체
     */
    private CodeExecutionResponseDto mapToResponseDto(CodeSnippet snippet) {
        return CodeExecutionResponseDto.builder()
                .id(snippet.getId())
                .status(snippet.getExecutionStatus())
                .build();
    }

    /**
     * 실행 실패 상태의 CodeExecutionResponseDto 객체를 생성
     *
     * @param id 실패한 코드 스니펫의 ID
     * @return 실행 실패 상태를 나타내는 CodeExecutionResponseDto 객체
     */
    private CodeExecutionResponseDto createFailResponseDto(Long id) {
        return CodeExecutionResponseDto.builder()
                .id(id)
                .status(ExecutionStatus.FAIL)
                .build();
    }
}
