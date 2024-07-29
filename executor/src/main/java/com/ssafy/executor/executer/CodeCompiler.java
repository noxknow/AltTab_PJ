package com.ssafy.executor.executer;

import com.ssafy.executor.common.enums.ExceptionMessage;
import com.ssafy.executor.common.enums.LogMessage;
import com.ssafy.executor.common.exception.CompileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodeCompiler {
    private static final long COMPILE_TIMEOUT_SECONDS = 10;

    /**
     * 코드 컴파일
     *
     * @param mainJavaFile
     * @throws Exception
     */
    public void compileCode(Path mainJavaFile) throws Exception {
        checkForbiddenPattern(Files.readString(mainJavaFile));

        Process compileProcess = startCompileProcess(mainJavaFile);
        String output = captureProcessOutput(compileProcess);

        boolean compilationSucceeded = waitForCompilation(compileProcess);

        if (!compilationSucceeded) {
            checkCompileResult(false, output);
        }

        log.info(LogMessage.COMPILE_SUCCESSFUL.getMessage());
    }

    /**
     * 컴파일 프로세스 생성 및 시작
     * @param mainJavaFile
     * @return process
     * @throws IOException
     */
    private Process startCompileProcess(Path mainJavaFile) throws IOException {
        ProcessBuilder compileProcessBuilder = new ProcessBuilder("javac", "-Xdiags:verbose", mainJavaFile.toString());
        compileProcessBuilder.redirectErrorStream(true);
        return compileProcessBuilder.start();
    }

    /**
     * 결과 캡쳐
     *
     * @param process
     * @return
     * @throws IOException
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
     * 컴파일 대기
     *
     * @param compileProcess
     * @return 성공여부
     * @throws Exception
     */
    private boolean waitForCompilation(Process compileProcess) throws Exception {
        if (!compileProcess.waitFor(COMPILE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            compileProcess.destroy();
            throw new CompileException(ExceptionMessage.COMPILATION_TIMEOUT.formatMessage(COMPILE_TIMEOUT_SECONDS));
        }
        return compileProcess.exitValue() == 0;
    }

    /**
     * 금지된 패턴 검사
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
     * 컴파일 성공 여부 확인
     *
     * @param success 컴파일 성공 여부
     * @param output 컴파일 출력 정보
     * @throws CompileException 컴파일 실패 시 발생
     */
    private void checkCompileResult(boolean success, String output) throws CompileException {
        if (!success) {
            String formattedErrors = formatCompileErrors(output);
            log.error(ExceptionMessage.COMPILATION_FAILED.formatMessage(formattedErrors));
            throw new CompileException(ExceptionMessage.COMPILATION_FAILED.formatMessage(formattedErrors));
        }
    }

    /**
     * 컴파일 에러 정보 포맷
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
     * 개별 에러 라인 포맷
     *
     * @param errorLine 에러 라인
     * @return 포맷된 에러 라인
     */
    private String formatErrorLine(String errorLine) {
        String[] parts = errorLine.split(":");
        if (parts.length >= 3) {
            String lineNumber = parts[1].trim();
            String errorMessage = String.join(":", Arrays.copyOfRange(parts, 3, parts.length)).trim();
            return String.format("Line %s: %s", lineNumber, errorMessage);
        }
        return errorLine;
    }
}