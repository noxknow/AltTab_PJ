package com.ssafy.alttab.common.security.controller;

import com.ssafy.alttab.common.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/refresh")
    public ResponseEntity<?> generateRefreshToken(HttpServletRequest request){
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokenService.generateRefreshToken(request).toString())
                .build();
    }
}
