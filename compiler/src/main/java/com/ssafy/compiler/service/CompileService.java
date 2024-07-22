package com.ssafy.compiler.service;


import com.ssafy.compiler.dto.CodeExecutionRequestDto;
import com.ssafy.compiler.dto.CodeExecutionResponseDto;
import com.ssafy.compiler.executer.Compiler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompileService {

    private final Compiler compiler;

    public CodeExecutionResponseDto executeCode(CodeExecutionRequestDto request){
        return compiler.execute(request.getCode());
    }
}