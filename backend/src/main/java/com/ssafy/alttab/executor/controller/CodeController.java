package com.ssafy.alttab.executor.controller;

import com.ssafy.alttab.executor.dto.CodeExecutionRequestDto;
import com.ssafy.alttab.executor.dto.CodeExecutionResponseDto;
import com.ssafy.alttab.executor.dto.CodeResponseDto;
import com.ssafy.alttab.executor.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/executor")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @PostMapping("/execute")
    public ResponseEntity<CodeExecutionResponseDto> executeCode(@RequestBody CodeExecutionRequestDto request) {
        return new ResponseEntity<>(codeService.executeCodeAsync(request), HttpStatus.OK);
    }

    @GetMapping("/{studyGroupId}/{problemId}/{problemTab}")
    public ResponseEntity<CodeResponseDto> getCode(
            @PathVariable Long studyGroupId,
            @PathVariable Long problemId,
            @PathVariable Long problemTab){
        return new ResponseEntity<>(codeService.getCode(studyGroupId,problemId,problemTab), HttpStatus.OK);
    }

    @GetMapping("/status/{studyGroupId}/{problemId}/{problemTab}")
    public ResponseEntity<CodeExecutionResponseDto> getExecutionStatus(
            @PathVariable Long studyGroupId,
            @PathVariable Long problemId,
            @PathVariable Long problemTab) {
        return new ResponseEntity<>(codeService.getExecutionResult(studyGroupId, problemId, problemTab), HttpStatus.OK);
    }
}
