package com.ssafy.compiler.controller;


import com.ssafy.compiler.dto.CodeExecutionRequestDto;
import com.ssafy.compiler.service.CompileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/compiler")
@RequiredArgsConstructor
@Slf4j
public class CodeController {

    private final CompileService compileService;

    @PostMapping("/execute")
    public ResponseEntity<?> executeCode(@RequestBody CodeExecutionRequestDto request){
        return new ResponseEntity<>(compileService.executeCode(request), HttpStatus.OK);
    }
}
