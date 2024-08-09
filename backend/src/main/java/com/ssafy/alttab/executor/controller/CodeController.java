package com.ssafy.alttab.executor.controller;

import com.ssafy.alttab.executor.dto.CodeExecutionRequestDto;
import com.ssafy.alttab.executor.dto.CodeExecutionResponseDto;
import com.ssafy.alttab.executor.dto.CodeResponseDto;
import com.ssafy.alttab.executor.service.CodeService;
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
@RequestMapping("/api/v1/executor")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @PostMapping("/execute")
    public ResponseEntity<CodeExecutionResponseDto> executeCode(@RequestBody CodeExecutionRequestDto request) {
        return new ResponseEntity<>(codeService.executeCodeAsync(request), HttpStatus.OK);
    }

    @GetMapping("/{studyId}/{problemId}/{memberId}")
    public ResponseEntity<CodeResponseDto> getCode(
            @PathVariable Long studyId,
            @PathVariable Long problemId,
            @PathVariable Long memberId) {
        return new ResponseEntity<>(codeService.getCode(studyId, problemId, memberId), HttpStatus.OK);
    }

    @GetMapping("/status/{studyId}/{problemId}/{memberId}")
    public ResponseEntity<CodeExecutionResponseDto> getExecutionStatus(
            @PathVariable Long studyId,
            @PathVariable Long problemId,
            @PathVariable Long memberId) {
        return new ResponseEntity<>(codeService.getExecutionResult(studyId, problemId, memberId), HttpStatus.OK);
    }
}
