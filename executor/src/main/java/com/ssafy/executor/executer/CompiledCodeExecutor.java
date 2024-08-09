package com.ssafy.executor.executer;

import com.ssafy.executor.common.enums.ExceptionMessage;
import com.ssafy.executor.common.enums.LogMessage;
import com.ssafy.executor.common.exception.CodeExecutionException;
import com.ssafy.executor.dto.CodeExecutionRequestDto;
import com.ssafy.executor.dto.CodeExecutionResponseDto;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompiledCodeExecutor {
    private static final String MAIN_CLASS_NAME = "Main";
    private static final long TIMEOUT_SECONDS = 2;

    private final CodeCompiler codeCompiler;

    /**
     * @param request
     * @return
     * @throws Exception
     */
    public CodeExecutionResponseDto executeCode(CodeExecutionRequestDto request) throws Exception {
        Path tempDir = createTempDirectory();
        try {
            Path mainJavaFile = writeCodeToFile(tempDir, request.getCode());
            compileCode(mainJavaFile, request);
            return runCode(request, tempDir, request.getInput());
        } finally {
            cleanupTempDirectory(tempDir);
        }
    }

    /**
     * 임시 디렉토리 생성
     *
     * @return 생성된 임시 디렉토리의 Path
     * @throws IOException 디렉토리 생성 실패 시 발생
     */
    private Path createTempDirectory() throws IOException {
        return Files.createTempDirectory("code_execution");
    }

    /**
     * 코드를 파일로 작성
     *
     * @param tempDir 임시 디렉토리 경로
     * @param code    작성할 코드 문자열
     * @return 작성된 Java 파일의 Path
     * @throws IOException 파일 작성 실패 시 발생
     */
    private Path writeCodeToFile(Path tempDir, String code) throws IOException {
        Path mainJavaFile = tempDir.resolve("Main.java");
        Files.write(mainJavaFile, code.getBytes());
        return mainJavaFile;
    }

    /**
     * 코드 컴파일
     *
     * @param mainJavaFile 컴파일할 Java 파일의 Path
     * @throws Exception 컴파일 실패 시 발생
     */
    private void compileCode(Path mainJavaFile, CodeExecutionRequestDto request) throws Exception {
        log.info(LogMessage.COMPILE_STARTED.getMessage(), mainJavaFile);
        codeCompiler.compileCode(mainJavaFile, request);
    }

    /**
     * 컴파일된 코드 실행
     *
     * @param tempDir 컴파일된 클래스 파일이 있는 디렉토리 경로
     * @param input   프로그램에 제공할 입력 문자열
     * @return 코드 실행 결과
     * @throws IOException 프로세스 실행 또는 입출력 오류 시 발생
     */
    private CodeExecutionResponseDto runCode(CodeExecutionRequestDto request, Path tempDir, String input)
            throws Exception {
        log.info(LogMessage.EXECUTION_STARTED.getMessage(), input);
        ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", tempDir.toString(), MAIN_CLASS_NAME);
        runProcessBuilder.redirectErrorStream(true);
        Process runProcess = runProcessBuilder.start();

        provideInput(runProcess, input);

        String output = executeWithTimeout(runProcess, request);
        return handleProcessResult(request, runProcess, output);
    }

    /**
     * 실행 중인 프로세스에 입력 제공
     *
     * @param process 입력을 받을 프로세스
     * @param input   제공할 입력 문자열
     * @throws IOException 입력 제공 중 오류 발생 시
     */
    private void provideInput(Process process, String input) throws IOException {
        if (!input.isEmpty()) {
            try (OutputStream os = process.getOutputStream()) {
                os.write(input.getBytes());
                os.flush();
            }
        }
    }

    /**
     * 프로세스 실행 및 타임아웃 처리
     *
     * @param process 실행할 프로세스
     * @return 프로세스의 출력 문자열
     * @throws TimeoutException     실행 시간이 초과된 경우
     * @throws ExecutionException   실행 중 예외가 발생한 경우
     * @throws InterruptedException 실행이 중단된 경우
     */
    private String executeWithTimeout(Process process, CodeExecutionRequestDto request)
            throws ExecutionException, InterruptedException {
        StringBuilder output = new StringBuilder();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<String> future = executor.submit(() -> readProcessOutput(process, output));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return output.toString();
        } catch (TimeoutException e) {
            throw new CodeExecutionException(ExceptionMessage.EXECUTION_TIMEOUT.formatMessage(TIMEOUT_SECONDS),
                    request);
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * 프로세스의 출력을 읽음
     *
     * @param process 출력을 읽을 프로세스
     * @param output  출력을 저장할 StringBuilder
     * @return 프로세스의 전체 출력 문자열
     * @throws IOException 입출력 오류 발생 시
     */
    private String readProcessOutput(Process process, StringBuilder output) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }


    /**
     * 프로세스 실행 결과 처리
     *
     * @param process 실행된 프로세스
     * @param output  프로세스의 출력 문자열
     * @return 코드 실행 결과를 담은 CodeExecutionResponseDto
     */
    private CodeExecutionResponseDto handleProcessResult(CodeExecutionRequestDto request, Process process,
                                                         String output)
            throws InterruptedException {
        waitForProcessCompletion(process);
        int exitValue = getProcessExitValue(process);

        if (exitValue != 0) {
            return handleFailedExecution(request, exitValue);
        }

        return handleSuccessfulExecution(request, output);
    }

    /**
     * 프로세스 완료를 대기
     *
     * @param process 대기할 프로세스
     * @throws InterruptedException 대기 중 인터럽트 발생 시
     */
    private void waitForProcessCompletion(Process process) throws InterruptedException {
        if (process.isAlive()) {
            log.info("Process is still alive, waiting for 100ms");
            process.waitFor(100, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 프로세스의 종료 코드를 반환
     *
     * @param process 종료 코드를 확인할 프로세스
     * @return 프로세스의 종료 코드
     */
    private int getProcessExitValue(Process process) {
        int exitValue = process.exitValue();
        log.info("Process exit value: {}", exitValue);
        return exitValue;
    }

    /**
     * 실행 실패 시 응답 처리
     *
     * @param request   코드 실행 요청 DTO
     * @param exitValue 프로세스 종료 코드
     * @return 실행 실패에 대한 응답 DTO
     */
    private CodeExecutionResponseDto handleFailedExecution(CodeExecutionRequestDto request, int exitValue) {
        log.error(LogMessage.EXECUTION_FAILED.getMessage(),
                ExceptionMessage.EXECUTION_FAILED_WITH_EXIT_CODE.formatMessage(exitValue));
        return CodeExecutionResponseDto.builder()
                .studyId(request.getStudyId())
                .problemId(request.getProblemId())
                .memberId(request.getMemberId())
                .errorMessage(ExceptionMessage.EXECUTION_FAILED_WITH_EXIT_CODE.formatMessage(exitValue))
                .build();
    }

    /**
     * 실행 성공 시 응답 처리
     *
     * @param request 코드 실행 요청 DTO
     * @param output  프로세스 실행 결과 출력
     * @return 실행 성공에 대한 응답 DTO
     */
    private CodeExecutionResponseDto handleSuccessfulExecution(CodeExecutionRequestDto request, String output) {
        log.info(LogMessage.EXECUTION_SUCCESSFUL.getMessage());
        return CodeExecutionResponseDto.builder()
                .studyId(request.getStudyId())
                .problemId(request.getProblemId())
                .memberId(request.getMemberId())
                .output(output)
                .build();
    }

    /**
     * 임시 디렉토리 정리
     *
     * @param tempDir 정리할 임시 디렉토리의 Path
     */
    private void cleanupTempDirectory(Path tempDir) {
        try {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            log.error("Failed to cleanup temporary directory", e);
        }
    }
}