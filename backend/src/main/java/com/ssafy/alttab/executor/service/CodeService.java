package com.ssafy.alttab.executor.service;

import com.ssafy.alttab.common.exception.CodeNotFoundException;
import com.ssafy.alttab.executor.dto.CodeExecutionRequestDto;
import com.ssafy.alttab.executor.dto.CodeExecutionResponseDto;
import com.ssafy.alttab.executor.dto.CodeResponseDto;
import com.ssafy.alttab.executor.entity.CodeSnippet;
import com.ssafy.alttab.executor.enums.ExecutionStatus;
import com.ssafy.alttab.executor.repository.CodeSnippetRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final RabbitTemplate rabbitTemplate;
    private final CodeSnippetRepository codeSnippetRepository;
    private final CacheManager cacheManager;
    private final SimpMessagingTemplate messagingTemplate;


    @Transactional
    public CodeResponseDto getCode(Long studyId, Long problemId, Long memberId) {
        Optional<CodeSnippet> code = codeSnippetRepository.findByStudyIdAndProblemIdAndMemberId(
                studyId, problemId, memberId);
        if (code.isPresent()) {
            return CodeResponseDto.builder()
                    .code(code.get().getCode())
                    .build();
        }

        throw new CodeNotFoundException(studyId, problemId, memberId);
    }

    /**
     * 코드와 실행 ID를 저장하거나 업데이트
     *
     * @param request 요청 dto (코드, studyId, problemId, memberId )
     * @return 저장된 코드 스니펫의 ID
     */
    @Transactional
    public CodeSnippet saveCode(CodeExecutionRequestDto request) {
        CodeSnippet codeSnippet = updateCodeIfExisting(request);
        return codeSnippetRepository.save(codeSnippet);
    }

    /**
     * 기존 코드 스니펫을 업데이트하거나 새로운 코드 스니펫을 생성
     *
     * @param request 코드 실행 요청 DTO
     * @return 업데이트되거나 새로 생성된 CodeSnippet 객체
     */
    public CodeSnippet updateCodeIfExisting(CodeExecutionRequestDto request) {
        return codeSnippetRepository.findByStudyIdAndProblemIdAndMemberId(
                        request.getStudyId(), request.getProblemId(), request.getMemberId())
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
     * 새로운 CodeSnippet 객체를 생성
     *
     * @param request 코드 실행 요청 DTO
     * @return 새로 생성된 CodeSnippet 객체
     */
    private CodeSnippet createNewCodeSnippet(CodeExecutionRequestDto request) {
        return CodeSnippet.builder()
                .studyId(request.getStudyId())
                .problemId(request.getProblemId())
                .memberId(request.getMemberId())
                .code(request.getCode())
                .executionStatus(ExecutionStatus.NOT_START)
                .build();
    }

    /**
     * 코드를 비동기적으로 실행하고 초기 응답
     *
     * @param request 코드 실행 요청 DTO
     * @return 초기 코드 실행 응답 DTO
     */
    @Transactional
    public CodeExecutionResponseDto executeCodeAsync(CodeExecutionRequestDto request, UserDetails userDetails) {
        CodeSnippet savedSnippet = saveCode(request);
        request.changeUserDetails(userDetails.getUsername());

        CodeExecutionResponseDto response = CodeExecutionResponseDto.builder()
                .studyId(savedSnippet.getStudyId())
                .problemId(savedSnippet.getProblemId())
                .memberId(savedSnippet.getMemberId())
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
    @Transactional
    @RabbitListener(queues = "code-execution-response-queue")
    public void receive(CodeExecutionResponseDto response) {
        codeSnippetRepository.findByStudyIdAndProblemIdAndMemberId(
                        response.getStudyId(), response.getProblemId(), response.getMemberId())
                .ifPresent(snippet -> processExecutionResponse(snippet, response));

        sendResponseWithSocket(response);
    }

    @Transactional
    public void sendResponseWithSocket(CodeExecutionResponseDto response){
        String destination = String.format("/sub/api/v1/executor/execute/%d/%d/%d",
                response.getStudyId(), response.getProblemId(), response.getMemberId());
        messagingTemplate.convertAndSend(destination, getExecutionResult(response.getStudyId(), response.getProblemId(), response.getMemberId()));
    }

    /**
     * 코드 실행 결과를 처리하고 캐시에 저장하는 메서드
     *
     * @param snippet  코드 스니펫 엔티티
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
     * 코드 스니펫의 실행 결과를 캐시에 저장
     *
     * @param codeSnippet 코드 스니펫 객체
     * @param response    코드 실행 결과 DTO
     */
    private void cacheOutput(CodeSnippet codeSnippet, CodeExecutionResponseDto response) {
        Cache cache = cacheManager.getCache("outputCache");
        if (cache != null) {
            cache.put(getCacheKey(codeSnippet, response.getRunUsername()), response);
        }
    }

    /**
     * 주어진 파라미터에 해당하는 코드 실행 결과를 조회
     *
     * @param studyId 스터디 그룹 ID
     * @param problemId    문제 ID
     * @param memberId   문제 탭
     * @return 코드 실행 결과 DTO
     */
    public CodeExecutionResponseDto getExecutionResult(Long studyId, Long problemId, Long memberId, UserDetails userDetails) {
        String runUsername = userDetails.getUsername();
        return codeSnippetRepository.findByStudyIdAndProblemIdAndMemberId(studyId, problemId, memberId)
                .map(snippet -> processAndGetExecutionResult(snippet, runUsername))
                .orElseGet(() -> createFailResponseDto(studyId, problemId, memberId));
    }


    /**
     * 코드 스니펫을 처리하고 실행 결과를 반환
     *
     * @param snippet 처리할 코드 스니펫
     * @return 실행 결과 DTO, 또는 실행 상태가 DONE이 아닌 경우 null
     */
    private CodeExecutionResponseDto processAndGetExecutionResult(CodeSnippet snippet, String runUsername) {
        CodeExecutionResponseDto response = createResponseDtoFromSnippet(snippet, runUsername);
        if (response.getStatus() == ExecutionStatus.DONE) {
            updateExecutionStatus(snippet);
            return response;
        }

        if (response.getStatus() == ExecutionStatus.NOT_START) {
            return CodeExecutionResponseDto.builder()
                    .status(ExecutionStatus.NOT_START)
                    .build();
        }

        return null;
    }

    /**
     * 코드 스니펫의 실행 상태를 NOT_START로 업데이트
     *
     * @param snippet 업데이트할 코드 스니펫
     */
    private void updateExecutionStatus(CodeSnippet snippet) {
        snippet.changeExecutionStatus(ExecutionStatus.NOT_START);
        codeSnippetRepository.save(snippet);
    }

    /**
     * 코드 스니펫 엔티티로부터 실행 결과 DTO를 생성하는 메서드
     *
     * @param snippet 코드 스니펫 엔티티
     * @return 생성된 코드 실행 결과 DTO
     */
    private CodeExecutionResponseDto createResponseDtoFromSnippet(CodeSnippet snippet, String runUsername) {
        CodeExecutionResponseDto result = getOutputFromCache(snippet, runUsername);
        return CodeExecutionResponseDto.builder()
                .studyId(snippet.getStudyId())
                .problemId(snippet.getProblemId())
                .memberId(snippet.getMemberId())
                .status(snippet.getExecutionStatus())
                .output(result != null ? result.getOutput() : null)
                .errorMessage(result != null ? result.getErrorMessage() : null)
                .build();
    }

    /**
     * 캐시에서 코드 스니펫의 실행 결과를 조회
     *
     * @param codeSnippet 조회할 코드 스니펫
     * @return 캐시된 실행 결과 DTO, 또는 캐시에 없는 경우 null
     */
    private CodeExecutionResponseDto getOutputFromCache(CodeSnippet codeSnippet, String runUsername) {
        Cache cache = cacheManager.getCache("outputCache");
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(getCacheKey(codeSnippet, runUsername));
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
     * 실행 실패 상태의 CodeExecutionResponseDto를 생성
     *
     * @param studyId 스터디 그룹 ID
     * @param problemId    문제 ID
     * @param memberId   문제 탭
     * @return 실행 실패 상태로 설정된 CodeExecutionResponseDto 객체
     */
    private CodeExecutionResponseDto createFailResponseDto(Long studyId, Long problemId, Long memberId) {
        return CodeExecutionResponseDto.builder()
                .studyId(studyId)
                .problemId(problemId)
                .memberId(memberId)
                .status(ExecutionStatus.FAIL)
                .build();
    }

    /**
     * 코드 스니펫의 캐시 키를 생성
     *
     * @param codeSnippet 키를 생성할 코드 스니펫
     * @return 생성된 캐시 키 문자열
     */
    private String getCacheKey(CodeSnippet codeSnippet, String runUsername) {
        return String.format("%d:%d:%d:%s", codeSnippet.getStudyId(), codeSnippet.getProblemId(),
                codeSnippet.getMemberId(), runUsername);
    }
}
