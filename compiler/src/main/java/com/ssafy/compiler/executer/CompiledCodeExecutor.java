package com.ssafy.compiler.executer;

import com.ssafy.compiler.common.enums.LogMessage;
import com.ssafy.compiler.common.util.StreamRedirector;
import com.ssafy.compiler.dto.CodeExecutionResponseDto;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CompiledCodeExecutor {
    private static final String MAIN_CLASS_NAME = "Main";
    private static final String MAIN_METHOD_NAME = "main";


    /**
     * 컴파일 된 코드 실행
     *
     * @param input
     * @return output, errorMessage
     * @throws Exception
     */
    public CodeExecutionResponseDto executeCode(String input) throws Exception {
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(".").toURI().toURL()});
        Class<?> cls = classLoader.loadClass(MAIN_CLASS_NAME);
        Method method = cls.getMethod(MAIN_METHOD_NAME, String[].class);

        return runWithRedirectedIO(input, () -> method.invoke(null, (Object) new String[]{}));
    }

    /**
     * 표준 입력으로 리디렉션
     *
     * @param input
     * @param runnable
     * @return output, errorMessage
     */
    private CodeExecutionResponseDto runWithRedirectedIO(String input, IORunnable runnable) {
        StreamRedirector redirect = new StreamRedirector(input);
        try {
            redirect.redirect();
            return executeAndCaptureOutput(runnable, redirect.getOutputStream(), redirect.getErrorStream());
        } finally {
            redirect.restore();
        }
    }

    /**
     * 표준 출력과 표준 오류를 캡쳐
     *
     * @param runnable
     * @param outputStream
     * @param errorStream
     * @return output, errorMessage
     */
    private CodeExecutionResponseDto executeAndCaptureOutput(IORunnable runnable,
                                                             ByteArrayOutputStream outputStream,
                                                             ByteArrayOutputStream errorStream) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.error(LogMessage.EXECUTION_FAILED.getMessage(), e.getMessage());
            try {
                errorStream.write(getStackTrace(e).getBytes());
            } catch (IOException ioException) {
                log.error("Failed to write stack trace to error stream", ioException);
            }
        }
        return buildResponse(outputStream, errorStream);
    }

    /**
     * response dto set
     *
     * @param outputStream
     * @param errorStream
     * @return output, errorMessage
     */
    private CodeExecutionResponseDto buildResponse(ByteArrayOutputStream outputStream,
                                                   ByteArrayOutputStream errorStream) {
        String output = outputStream.toString();
        String error = errorStream.toString();
        if (error.isEmpty()) {
            return CodeExecutionResponseDto.builder().output(output).build();
        } else {
            return CodeExecutionResponseDto.builder().errorMessage(error).build();
        }
    }

    /**
     * 에러난 줄 추적해서 build
     *
     * @param throwable
     * @return 에러난 줄과 에러메시지
     */
    private String getStackTrace(Throwable throwable) {
        StringBuilder result = new StringBuilder();
        result.append(throwable.toString()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            if (element.getClassName().equals("Main")) {
                result.append("\tat ").append(element).append("\n");
            }
        }
        return result.toString();
    }

    @FunctionalInterface
    private interface IORunnable {
        void run() throws Exception;
    }

}
