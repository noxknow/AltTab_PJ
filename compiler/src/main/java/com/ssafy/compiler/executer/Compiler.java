package com.ssafy.compiler.executer;

import com.ssafy.compiler.dto.CodeExecutionResponseDto;
import java.net.URISyntaxException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class Compiler {

    private String code;

    public CodeExecutionResponseDto execute(String code) {
        this.code = code;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
             PrintStream errPrintStream = new PrintStream(errorStream)) {

            boolean compiledSuccessfully = compileCode(errPrintStream);

            if (!compiledSuccessfully) {
                return buildResponse(outputStream, errorStream);
            }

            return runCode(outputStream, errorStream);
        } catch (Exception e) {
            return CodeExecutionResponseDto.builder().errorMessage(e.getMessage()).build();
        }
    }

    private boolean compileCode(PrintStream errPrintStream) {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileObject fileObject = new JavaSourceFromString("Main", code);
        Iterable<? extends JavaFileObject> compilationUnits = List.of(fileObject);

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

        boolean success = task.call();
        if (!success) {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                if (diagnostic.getSource() != null && diagnostic.getSource().getName().contains("Main")) {
                    errPrintStream.println(diagnostic.toString());
                }
            }
        }
        return success;
    }

    private CodeExecutionResponseDto runCode(ByteArrayOutputStream outputStream, ByteArrayOutputStream errorStream) throws IOException, URISyntaxException, ClassNotFoundException {
        File classDir = new File(".");

        try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classDir.toURI().toURL()})) {
            Class<?> cls = Class.forName("Main", true, classLoader);
            Method method = cls.getMethod("main", String[].class);

            PrintStream originalOut = System.out;
            PrintStream originalErr = System.err;

            try {
                System.setOut(new PrintStream(outputStream));
                System.setErr(new PrintStream(errorStream));

                try {
                    method.invoke(null, (Object) new String[]{});
                } catch (InvocationTargetException e) {
                    Throwable targetException = e.getTargetException();
                    errorStream.write(getStackTrace(targetException).getBytes());
                } catch (Throwable e) {
                    errorStream.write(getStackTrace(e).getBytes());
                }
            } finally {
                System.setOut(originalOut);
                System.setErr(originalErr);
            }
        } catch (Exception e) {
            errorStream.write(e.toString().getBytes());
        }
        return buildResponse(outputStream, errorStream);
    }

    private CodeExecutionResponseDto buildResponse(ByteArrayOutputStream outputStream, ByteArrayOutputStream errorStream) {
        String output = outputStream.toString();
        String error = errorStream.toString();
        if (error.isEmpty()) {
            return CodeExecutionResponseDto.builder().output(output).build();
        } else {
            return CodeExecutionResponseDto.builder().errorMessage(error).build();
        }
    }

    private String getStackTrace(Throwable throwable) {
        StringBuilder result = new StringBuilder();
        result.append(throwable.toString()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            if (element.getClassName().equals("Main")) {
                result.append("\tat ").append(element.toString()).append("\n");
            }
        }
        return result.toString();
    }

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
