package com.ssafy.executor.executer;

import com.ssafy.executor.common.enums.ExceptionMessage;
import com.ssafy.executor.common.enums.LogMessage;
import com.ssafy.executor.common.exception.CompileException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CodeCompiler {

    private static final String MAIN_CLASS_NAME = "Main";

    private final JavaCompiler compiler;

    public CodeCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
    }

    /**
     * 전체 코드 컴파일 과정
     *
     * @param sourceCode 컴파일할 소스 코드
     * @throws CompileException 컴파일 실패 시 발생
     */
    public void compileCode(String sourceCode) throws CompileException {
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        checkForbiddenPattern(sourceCode);
        boolean success = performCompile(sourceCode, diagnostics);
        checkCompileResult(success, diagnostics);
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
     * 코드 컴파일 수행
     *
     * @param sourceCode 컴파일할 소스 코드
     * @param diagnostics 디버깅 과정에서 진단데이터 수집 목적
     * @return 컴파일 수행 완료 여부
     */
    private boolean performCompile(String sourceCode, DiagnosticCollector<JavaFileObject> diagnostics) {
        JavaFileObject fileObject = new JavaSourceFromString(MAIN_CLASS_NAME, sourceCode);
        Iterable<? extends JavaFileObject> compileUnits = List.of(fileObject);

        JavaCompiler.CompilationTask task = compiler.getTask(
                null, null, diagnostics, null, null, compileUnits);

        return task.call();
    }

    /**
     * 컴파일 성공 여부 확인
     *
     * @param success 컴파일 성공 여부
     * @param diagnostics 컴파일 진단 정보
     * @throws CompileException 컴파일 실패 시 발생
     */
    private void checkCompileResult(boolean success, DiagnosticCollector<JavaFileObject> diagnostics)
            throws CompileException {
        if (!success) {
            String errors = diagnostics.getDiagnostics().stream()
                    .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
                    .map(this::formatDiagnostic)
                    .collect(Collectors.joining("\n"));
            log.error(ExceptionMessage.COMPILATION_FAILED.formatMessage(errors));
            throw new CompileException(ExceptionMessage.COMPILATION_FAILED.formatMessage(errors));
        }

        log.info(LogMessage.COMPILE_SUCCESSFUL.getMessage());
    }

    /**
     * 진단 에러 정보 포맷
     *
     * @param diagnostic 진단 정보
     * @return 포맷된 진단 에러 메시지
     */
    private String formatDiagnostic(Diagnostic<?> diagnostic) {
        return String.format("Line %d: %s",
                diagnostic.getLineNumber(),
                diagnostic.getMessage(null).replaceFirst(".*error: ", ""));
    }

    /**
     * String to Java Source
     */
    static public class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        public JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}