package com.ssafy.executor.executer;

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
    private static final String MAIN_METHOD_NAME = "main";

    private final JavaCompiler compiler;

    public CodeCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
    }

    /**
     * 전체 코드 컴파일 과정
     *
     * @param sourceCode
     * @throws CompileException
     */
    public void compileCode(String sourceCode) throws CompileException {
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        boolean success = performCompile(sourceCode, diagnostics);
        checkCompileResult(success, diagnostics);
    }

    /**
     * 코드 컴파일 수행
     *
     * @param sourceCode
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
     * @param success
     * @param diagnostics
     * @throws CompileException 실패시 exception throw
     */
    private void checkCompileResult(boolean success, DiagnosticCollector<JavaFileObject> diagnostics)
            throws CompileException {
        if (!success) {
            String errors = diagnostics.getDiagnostics().stream()
                    .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
                    .map(this::formatDiagnostic)
                    .collect(Collectors.joining("\n"));
            log.error(LogMessage.COMPILE_FAILED.getMessage(), errors);
            throw new CompileException(errors);
        }

        log.info(LogMessage.COMPILE_SUCCESSFUL.getMessage());
    }

    /**
     * 진단 에러 정보 이쁘게 출력하기 위한 메서드
     *
     * @param diagnostic
     * @return 포멧된 진단 에러 메시지
     */
    private String formatDiagnostic(Diagnostic<?> diagnostic) {
        return String.format("Line %d: %s",
                diagnostic.getLineNumber(),
                diagnostic.getMessage(null).replaceFirst(".*error: ", ""));
    }

    /**
     * String -> java
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
