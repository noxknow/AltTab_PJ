package com.ssafy.executor.executer;

import com.ssafy.executor.common.enums.ExceptionMessage;
import com.ssafy.executor.common.enums.LogMessage;
import com.ssafy.executor.dto.CodeExecutionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompiledCodeExecutor {
    private static final String MAIN_CLASS_NAME = "Main";
    private static final long TIMEOUT_SECONDS = 2;

    private final CodeCompiler codeCompiler;

    /**
     * 코드 실행
     *
     * @param code
     * @param input
     * @return 실행결과
     * @throws Exception
     */
    public CodeExecutionResponseDto executeCode(String code, String input) throws Exception {
        Path tempDir = createTempDirectory();
        try {
            Path mainJavaFile = writeCodeToFile(tempDir, code);
            compileCode(mainJavaFile);
            return runCode(tempDir, input);
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
     * @param code 작성할 코드 문자열
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
    private void compileCode(Path mainJavaFile) throws Exception {
        log.info(LogMessage.COMPILE_STARTED.getMessage(), mainJavaFile);
        codeCompiler.compileCode(mainJavaFile);
    }

    /**
     * 컴파일된 코드 실행
     *
     * @param tempDir 컴파일된 클래스 파일이 있는 디렉토리 경로
     * @param input   프로그램에 제공할 입력 문자열
     * @return 코드 실행 결과
     * @throws IOException 프로세스 실행 또는 입출력 오류 시 발생
     */
    private CodeExecutionResponseDto runCode(Path tempDir, String input) throws IOException, TimeoutException, ExecutionException, InterruptedException {
        log.info(LogMessage.EXECUTION_STARTED.getMessage(), input);
        ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", tempDir.toString(), MAIN_CLASS_NAME);
        runProcessBuilder.redirectErrorStream(true);
        Process runProcess = runProcessBuilder.start();

        provideInput(runProcess, input);

        String output = executeWithTimeout(runProcess);
        return handleProcessResult(runProcess, output);
    }

    /**
     * 실행 중인 프로세스에 입력 제공
     *
     * @param process 입력을 받을 프로세스
     * @param input 제공할 입력 문자열
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
     * @throws TimeoutException 실행 시간이 초과된 경우
     * @throws ExecutionException 실행 중 예외가 발생한 경우
     * @throws InterruptedException 실행이 중단된 경우
     */
    private String executeWithTimeout(Process process) throws TimeoutException, ExecutionException, InterruptedException {
        StringBuilder output = new StringBuilder();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<String> future = executor.submit(() -> readProcessOutput(process, output));
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return output.toString();
        } catch (TimeoutException e) {
            throw new TimeoutException(ExceptionMessage.EXECUTION_TIMEOUT.formatMessage(TIMEOUT_SECONDS));
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * 프로세스의 출력을 읽음
     *
     * @param process 출력을 읽을 프로세스
     * @param output 출력을 저장할 StringBuilder
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
     * @param output 프로세스의 출력 문자열
     * @return 코드 실행 결과를 담은 CodeExecutionResponseDto
     */
    private CodeExecutionResponseDto handleProcessResult(Process process, String output) {
        if (process.exitValue() != 0) {
            log.error(LogMessage.EXECUTION_FAILED.getMessage(), ExceptionMessage.EXECUTION_FAILED_WITH_EXIT_CODE.formatMessage(process.exitValue()));
            return CodeExecutionResponseDto.builder()
                    .errorMessage(ExceptionMessage.EXECUTION_FAILED_WITH_EXIT_CODE.formatMessage(process.exitValue()))
                    .build();
        }

        log.info(LogMessage.EXECUTION_SUCCESSFUL.getMessage(), output);
        return CodeExecutionResponseDto.builder()
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