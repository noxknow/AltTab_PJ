package com.ssafy.executor.executer;

import com.ssafy.executor.common.enums.ExceptionMessage;
import com.ssafy.executor.common.enums.LogMessage;
import com.ssafy.executor.common.exception.CompileException;
import com.ssafy.executor.dto.CodeExecutionRequestDto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodeCompiler {
    private static final long COMPILE_TIMEOUT_SECONDS = 10;

    /**
     * 주어진 Java 파일을 컴파일하고 실행
     *
     * @param mainJavaFile 컴파일할 메인 Java 파일의 경로
     * @param request 코드 실행 요청 정보를 담은 DTO
     * @throws Exception 컴파일 과정에서 발생할 수 있는 다양한 예외
     */
    public void compileCode(Path mainJavaFile, CodeExecutionRequestDto request) throws Exception {
        checkForbiddenPattern(Files.readString(mainJavaFile));

        Process compileProcess = startCompileProcess(mainJavaFile);
        String output = captureProcessOutput(compileProcess);

        boolean compilationSucceeded = waitForCompilation(compileProcess, request);

        if (!compilationSucceeded) {
            checkCompileResult(false, output, request);
        }

        log.info(LogMessage.COMPILE_SUCCESSFUL.getMessage());
    }

    /**
     * Java 컴파일 프로세스를 시작
     *
     * @param mainJavaFile 컴파일할 메인 Java 파일의 경로
     * @return 시작된 컴파일 프로세스
     * @throws IOException 프로세스 시작 중 I/O 오류 발생 시
     */
    private Process startCompileProcess(Path mainJavaFile) throws IOException {
        ProcessBuilder compileProcessBuilder = new ProcessBuilder("javac", "-Xdiags:verbose", mainJavaFile.toString());
        compileProcessBuilder.redirectErrorStream(true);
        return compileProcessBuilder.start();
    }

    /**
     * 주어진 프로세스의 출력을 캡처
     *
     * @param process 출력을 캡처할 프로세스
     * @return 캡처된 프로세스 출력 문자열
     * @throws IOException 출력 읽기 중 I/O 오류 발생 시
     */
    private String captureProcessOutput(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    /**
     * 컴파일 프로세스가 완료될 때까지 대기
     *
     * @param compileProcess 대기할 컴파일 프로세스
     * @param request 코드 실행 요청 정보를 담은 DTO
     * @return 컴파일 성공 여부
     * @throws Exception 컴파일 타임아웃 또는 기타 오류 발생 시
     */
    private boolean waitForCompilation(Process compileProcess, CodeExecutionRequestDto request) throws Exception {
        if (!compileProcess.waitFor(COMPILE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            compileProcess.destroy();
            throw new CompileException(ExceptionMessage.COMPILATION_TIMEOUT.formatMessage(COMPILE_TIMEOUT_SECONDS),
                    request);
        }
        return compileProcess.exitValue() == 0;
    }

    /**
     * 소스 코드에서 금지된 패턴을 검사
     *
     * @param sourceCode 검사할 소스 코드
     * @throws SecurityException 금지된 패턴 발견 시 발생
     */
    private void checkForbiddenPattern(String sourceCode) throws SecurityException {
        if (!CodeFilter.isSafeCode(sourceCode)) {
            List<String> violations = CodeFilter.getViolations(sourceCode);
            String violationsMessage = String.join(", ", violations);
            throw new SecurityException(ExceptionMessage.UNSAFE_CODE_DETECTED.formatMessage(violationsMessage));
        }
    }

    /**
     * 컴파일 결과를 확인하고 필요 시 예외를 발생
     *
     * @param success 컴파일 성공 여부
     * @param output 컴파일 출력 정보
     * @param request 코드 실행 요청 정보를 담은 DTO
     * @throws CompileException 컴파일 실패 시 발생
     */
    private void checkCompileResult(boolean success, String output, CodeExecutionRequestDto request)
            throws CompileException {
        if (!success) {
            String formattedErrors = formatCompileErrors(output);
            log.error(ExceptionMessage.COMPILATION_FAILED.formatMessage(formattedErrors));
            throw new CompileException(ExceptionMessage.COMPILATION_FAILED.formatMessage(formattedErrors), request);
        }
    }

    /**
     * 컴파일 에러 정보를 포맷
     *
     * @param output 컴파일 출력 정보
     * @return 포맷된 에러 메시지
     */
    private String formatCompileErrors(String output) {
        return Arrays.stream(output.split("\n"))
                .filter(line -> line.contains("error:"))
                .map(this::formatErrorLine)
                .collect(Collectors.joining("\n"));
    }

    /**
     * 개별 에러 라인을 포맷
     *
     * @param errorLine 포맷할 에러 라인
     * @return 포맷된 에러 라인
     */
    private String formatErrorLine(String errorLine) {
        // 정규표현식을 사용하여 필요한 정보만 추출
        Pattern pattern = Pattern.compile(".*?([^\\\\/:]+\\.java):(\\d+):(?:\\d+:)? error: (.+)");
        Matcher matcher = pattern.matcher(errorLine);

        if (matcher.find()) {
            String fileName = matcher.group(1);
            String lineNumber = matcher.group(2);
            String errorMessage = matcher.group(3);
            return String.format("%s Line %s: %s", fileName, lineNumber, errorMessage);
        }

        return errorLine;
    }
}