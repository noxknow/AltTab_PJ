package com.ssafy.alt_tab.compiler.controller;


import com.ssafy.alt_tab.compiler.dto.CodeExecutionRequestDto;
import com.ssafy.alt_tab.compiler.service.CodeService;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public CompletableFuture<ResponseEntity<?>> executeCode(@RequestBody CodeExecutionRequestDto request) {
        codeService.saveCode(request.getCode());
        return codeService.executeCode(request)
                .thenApply(ResponseEntity::ok);
    }
}
