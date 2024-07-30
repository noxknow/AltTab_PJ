package com.ssafy.alttab.compiler.controller;

import com.ssafy.alttab.compiler.dto.CodeExecutionRequestDto;
import com.ssafy.alttab.compiler.dto.CodeExecutionResponseDto;
import com.ssafy.alttab.compiler.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/compiler")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @PostMapping("/execute")
    public ResponseEntity<CodeExecutionResponseDto> executeCode(@RequestBody CodeExecutionRequestDto request) {
        Long codeId = codeService.saveCode(request.getCode(), request.getId());
        return new ResponseEntity<>(codeService.executeCodeAsync(request, codeId), HttpStatus.OK);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<CodeExecutionResponseDto> getExecutionStatus(@PathVariable Long id) {
        return new ResponseEntity<>(codeService.getExecutionResult(id), HttpStatus.OK);
    }
}
