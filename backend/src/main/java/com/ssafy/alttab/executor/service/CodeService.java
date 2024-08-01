package com.ssafy.alttab.executor.service;

import com.ssafy.alttab.executor.dto.CodeExecutionRequestDto;
import com.ssafy.alttab.executor.dto.CodeExecutionResponseDto;
import com.ssafy.alttab.executor.entity.CodeSnippet;
import com.ssafy.alttab.executor.enums.ExecutionStatus;
import com.ssafy.alttab.executor.repository.CodeSnippetRepository;
import jakarta.transaction.Transactional;
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
     * @param request 요청 dto (코드, studyGroupId, problemId, problemTab )
     * @return 저장된 코드 스니펫의 ID
     */
    @Transactional
    public CodeSnippet saveCode(CodeExecutionRequestDto request) {
        CodeSnippet codeSnippet = updateCodeIfExisting(request);
        return codeSnippetRepository.save(codeSnippet);
    }

    /**
     *
     * @param request
     * @return
     */
    public CodeSnippet updateCodeIfExisting(CodeExecutionRequestDto request) {
        return codeSnippetRepository.findByStudyGroupIdAndProblemIdAndProblemTab(
                        request.getStudyGroupId(), request.getProblemId(), request.getProblemTab())
                .map(snippet -> updateExistingCodeSnippet(snippet, request.getCode()))
                .orElseGet(() -> createNewCodeSnippet(request));
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
     *
     * @param request
     * @return
     */
    private CodeSnippet createNewCodeSnippet(CodeExecutionRequestDto request) {
        return CodeSnippet.builder()
                .studyGroupId(request.getStudyGroupId())
                .problemId(request.getProblemId())
                .problemTab(request.getProblemTab())
                .code(request.getCode())
                .executionStatus(ExecutionStatus.NOT_START)
                .build();
    }

    /**
     *
     * @param request
     * @return
     */
    public CodeExecutionResponseDto executeCodeAsync(CodeExecutionRequestDto request) {
        CodeSnippet savedSnippet = saveCode(request);
        CodeExecutionResponseDto response = CodeExecutionResponseDto.builder()
                .studyGroupId(savedSnippet.getStudyGroupId())
                .problemId(savedSnippet.getProblemId())
                .problemTab(savedSnippet.getProblemTab())
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
        codeSnippetRepository.findByStudyGroupIdAndProblemIdAndProblemTab(
                        response.getStudyGroupId(), response.getProblemId(), response.getProblemTab())
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
        cacheOutput(snippet, response);
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
     *
     * @param codeSnippet
     * @param response
     */
    private void cacheOutput(CodeSnippet codeSnippet, CodeExecutionResponseDto response) {
        Cache cache = cacheManager.getCache("outputCache");
        if (cache != null) {
            cache.put(getCacheKey(codeSnippet), response);
        }
    }

    /**
     *
     * @param studyGroupId
     * @param problemId
     * @param problemTab
     * @return
     */
    public CodeExecutionResponseDto getExecutionResult(Long studyGroupId, Long problemId, Long problemTab) {
        return codeSnippetRepository.findByStudyGroupIdAndProblemIdAndProblemTab(studyGroupId, problemId, problemTab)
                .map(this::createResponseDtoFromSnippet)
                .orElseGet(() -> createFailResponseDto(studyGroupId, problemId, problemTab));
    }

    /**
     * 코드 스니펫 엔티티로부터 실행 결과 DTO를 생성하는 메서드
     *
     * @param snippet 코드 스니펫 엔티티
     * @return 생성된 코드 실행 결과 DTO
     */
    private CodeExecutionResponseDto createResponseDtoFromSnippet(CodeSnippet snippet) {
        CodeExecutionResponseDto result = getOutputFromCache(snippet);
        return CodeExecutionResponseDto.builder()
                .studyGroupId(snippet.getStudyGroupId())
                .problemId(snippet.getProblemId())
                .problemTab(snippet.getProblemTab())
                .status(snippet.getExecutionStatus())
                .output(result != null ? result.getOutput() : null)
                .errorMessage(result != null ? result.getErrorMessage() : null)
                .build();
    }

    /**
     *
     * @param codeSnippet
     * @return
     */
    private CodeExecutionResponseDto getOutputFromCache(CodeSnippet codeSnippet) {
        Cache cache = cacheManager.getCache("outputCache");
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(getCacheKey(codeSnippet));
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
     *
     * @param studyGroupId
     * @param problemId
     * @param problemTab
     * @return
     */
    private CodeExecutionResponseDto createFailResponseDto(Long studyGroupId, Long problemId, Long problemTab) {
        return CodeExecutionResponseDto.builder()
                .studyGroupId(studyGroupId)
                .problemId(problemId)
                .problemTab(problemTab)
                .status(ExecutionStatus.FAIL)
                .build();
    }

    private String getCacheKey(CodeSnippet codeSnippet) {
        return String.format("%d:%d:%d", codeSnippet.getStudyGroupId(), codeSnippet.getProblemId(), codeSnippet.getProblemTab());
    }
}
