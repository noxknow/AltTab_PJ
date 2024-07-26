package com.ssafy.executor.executer;

import com.ssafy.executor.common.enums.ExceptionMessage;
import com.ssafy.executor.common.enums.LogMessage;
import com.ssafy.executor.dto.CodeExecutionResponseDto;
import java.util.Comparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

@Component
@Slf4j
public class CompiledCodeExecutor {
    private static final String MAIN_CLASS_NAME = "Main";
    private static final long TIMEOUT_SECONDS = 2;
    private static final long COMPILE_TIMEOUT_SECONDS = 10;

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

    private Path createTempDirectory() throws IOException {
        return Files.createTempDirectory("code_execution");
    }

    private Path writeCodeToFile(Path tempDir, String code) throws IOException {
        Path mainJavaFile = tempDir.resolve("Main.java");
        Files.write(mainJavaFile, code.getBytes());
        return mainJavaFile;
    }

    private void compileCode(Path mainJavaFile) throws Exception {
        log.info(LogMessage.COMPILE_STARTED.getMessage(), mainJavaFile);
        ProcessBuilder compileProcessBuilder = new ProcessBuilder("javac", mainJavaFile.toString());
        Process compileProcess = compileProcessBuilder.start();
        if (!compileProcess.waitFor(COMPILE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            compileProcess.destroy();
            throw new Exception(ExceptionMessage.COMPILATION_TIMEOUT.formatMessage(COMPILE_TIMEOUT_SECONDS));
        }
        if (compileProcess.exitValue() != 0) {
            throw new Exception(ExceptionMessage.COMPILATION_FAILED.formatMessage("Unknown error"));
        }
        log.info(LogMessage.COMPILE_SUCCESSFUL.getMessage());
    }

    private CodeExecutionResponseDto runCode(Path tempDir, String input) throws IOException {
        log.info(LogMessage.EXECUTION_STARTED.getMessage(), input);
        ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-cp", tempDir.toString(), MAIN_CLASS_NAME);
        runProcessBuilder.redirectErrorStream(true);
        Process runProcess = runProcessBuilder.start();

        provideInput(runProcess, input);

        return executeWithTimeout(runProcess);
    }

    private void provideInput(Process process, String input) throws IOException {
        if (!input.isEmpty()) {
            try (OutputStream os = process.getOutputStream()) {
                os.write(input.getBytes());
                os.flush();
            }
        }
    }

    private CodeExecutionResponseDto executeWithTimeout(Process process) {
        StringBuilder output = new StringBuilder();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> readProcessOutput(process, output));

        try {
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            return handleTimeout(process, future);
        } catch (InterruptedException | ExecutionException e) {
            return handleExecutionError(e);
        } finally {
            executor.shutdownNow();
        }

        return handleProcessResult(process, output.toString());
    }

    private String readProcessOutput(Process process, StringBuilder output) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    private CodeExecutionResponseDto handleTimeout(Process process, Future<?> future) {
        process.destroy();
        future.cancel(true);
        log.error(LogMessage.EXECUTION_FAILED.getMessage(), ExceptionMessage.EXECUTION_TIMEOUT.formatMessage(TIMEOUT_SECONDS));
        return CodeExecutionResponseDto.builder()
                .errorMessage(ExceptionMessage.EXECUTION_TIMEOUT.formatMessage(TIMEOUT_SECONDS))
                .build();
    }

    private CodeExecutionResponseDto handleExecutionError(Exception e) {
        log.error(LogMessage.EXECUTION_FAILED.getMessage(), e.getMessage());
        return CodeExecutionResponseDto.builder()
                .errorMessage(ExceptionMessage.UNEXPECTED_ERROR.formatMessage(e.getMessage()))
                .build();
    }

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