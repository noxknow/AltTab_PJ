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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final RabbitTemplate rabbitTemplate;
    private final CodeSnippetRepository codeSnippetRepository;
    private final CacheManager cacheManager;

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
     * RabbitMQ로부터 코드 실행 결과를 받아 처리하는 메서드
     *
     * @param response 코드 실행 결과를 담은 DTO
     */
    @RabbitListener(queues = "code-execution-response-queue")
    public void receive(CodeExecutionResponseDto response) {
        Long id = response.getId();
        codeSnippetRepository.findById(id)
                .ifPresent(snippet -> processExecutionResponse(snippet, response));
    }

    /**
     * 코드 실행 결과를 처리하고 캐시에 저장하는 메서드
     *
     * @param snippet 코드 스니펫 엔티티
     * @param response 코드 실행 결과를 담은 DTO
     */
    private void processExecutionResponse(CodeSnippet snippet, CodeExecutionResponseDto response) {
        updateCodeSnippet(snippet);
        cacheOutput(response.getId(), response);
    }

    /**
     * 코드 스니펫의 실행 상태를 업데이트하는 메서드
     *
     * @param snippet 업데이트할 코드 스니펫 엔티티
     */
    private void updateCodeSnippet(CodeSnippet snippet) {
        snippet.changeExecutionStatus(ExecutionStatus.DONE);
        codeSnippetRepository.save(snippet);
    }

    /**
     * 코드 실행 결과를 캐시에 저장하는 메서드
     *
     * @param id 코드 스니펫의 ID
     * @param response 캐시에 저장할 코드 실행 결과 DTO
     */
    private void cacheOutput(Long id, CodeExecutionResponseDto response) {
        Cache cache = cacheManager.getCache("outputCache");
        if (cache != null) {
            cache.put(id, response);
        }
    }

    /**
     * 주어진 ID에 해당하는 코드 실행 결과를 조회하는 메서드
     *
     * @param id 조회할 코드 스니펫의 ID
     * @return 코드 실행 결과를 담은 DTO
     */
    public CodeExecutionResponseDto getExecutionResult(Long id) {
        return codeSnippetRepository.findById(id)
                .map(this::createResponseDtoFromSnippet)
                .orElseGet(() -> createFailResponseDto(id));
    }

    /**
     * 코드 스니펫 엔티티로부터 실행 결과 DTO를 생성하는 메서드
     *
     * @param snippet 코드 스니펫 엔티티
     * @return 생성된 코드 실행 결과 DTO
     */
    private CodeExecutionResponseDto createResponseDtoFromSnippet(CodeSnippet snippet) {
        CodeExecutionResponseDto result = getOutputFromCache(snippet.getId());
        return CodeExecutionResponseDto.builder()
                .id(snippet.getId())
                .status(snippet.getExecutionStatus())
                .output(result.getOutput())
                .errorMessage(result.getErrorMessage())
                .build();
    }

    /**
     * 캐시에서 코드 실행 결과를 조회하는 메서드
     *
     * @param id 조회할 코드 스니펫의 ID
     * @return 캐시에서 조회된 코드 실행 결과 DTO, 없으면 null
     */
    private CodeExecutionResponseDto getOutputFromCache(Long id) {
        Cache cache = cacheManager.getCache("outputCache");
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(id);
            if (wrapper != null) {
                Object value = wrapper.get();
                if (value instanceof CodeExecutionResponseDto) {
                    return (CodeExecutionResponseDto) value;
                }
            }
        }
        return null;
    }

    /**
     * 실행 실패 상태의 응답 DTO를 생성하는 메서드
     *
     * @param id 실패한 코드 스니펫의 ID
     * @return 실행 실패 상태를 나타내는 코드 실행 결과 DTO
     */
    private CodeExecutionResponseDto createFailResponseDto(Long id) {
        return CodeExecutionResponseDto.builder()
                .id(id)
                .status(ExecutionStatus.FAIL)
                .build();
    }
}
