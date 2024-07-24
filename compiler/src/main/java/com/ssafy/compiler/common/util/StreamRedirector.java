package com.ssafy.compiler.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import lombok.Getter;

@Getter
public class StreamRedirector {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    private final InputStream inputStream;
    private PrintStream originalOut;
    private PrintStream originalErr;
    private InputStream originalIn;

    public StreamRedirector(String input) {
        this.inputStream = new ByteArrayInputStream(input.getBytes());
    }

    public void redirect() {
        originalOut = System.out;
        originalErr = System.err;
        originalIn = System.in;
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));
        System.setIn(inputStream);
    }

    public void restore() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }
}