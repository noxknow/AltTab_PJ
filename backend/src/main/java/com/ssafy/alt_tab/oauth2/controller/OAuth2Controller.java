package com.ssafy.alt_tab.oauth2.controller;

import com.ssafy.alt_tab.jwt.JWTUtil;
import com.ssafy.alt_tab.redis.service.BlackListService;
import com.ssafy.alt_tab.redis.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;

@Controller
@RequiredArgsConstructor
public class OAuth2Controller {
    private final TokenService tokenService;
    private final BlackListService blackListService;
    private final JWTUtil jwtUtil;

    //    @DeleteMapping("/auth/logout")
//    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) {
//        // 쿠키에서 Access Token 추출
//        String accessToken = extractAccessTokenFromCookie(request);
//
//        if (accessToken != null) {
//            // Access Token을 블랙리스트에 추가
//            blackListService.addToBlacklist(accessToken);
//            System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
//            tokenService.deleteRefreshToken(userDetails.getUsername());
//            // 쿠키에서 Access Token 제거
//            removeCookie(response, "Access-Token");
//        }
//
//        // Refresh Token 삭제
//        tokenService.deleteRefreshToken(userDetails.getUsername());
//
//        return ResponseEntity.noContent().build();
//    }
//@DeleteMapping("/auth/logout")
//public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    System.out.println("뭐임???");
//    if (authentication != null) {
//        Object principal = authentication.getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) principal;
//            System.out.println("Username: " + userDetails.getUsername());
//            // 로그아웃 로직 수행
//        } else if (principal instanceof String) {
//            System.out.println("Principal is a String: " + principal);
//        } else {
//            System.out.println("Principal type: " + (principal != null ? principal.getClass().getName() : "null"));
//        }
//    } else {
//        System.out.println("No authentication found in SecurityContext");
//    }
//
//    // 나머지 로그아웃 로직
//    return ResponseEntity.noContent().build();
//}
//    @DeleteMapping("/auth/logout")
//    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
//        String accessToken = extractAccessTokenFromCookie(request);
//        if (accessToken != null) {
//            // Access Token을 블랙리스트에 추가
//            blackListService.addToBlacklist(accessToken);
//            // Refresh Token 삭제
//            tokenService.deleteRefreshToken(jwtUtil.getUsername(accessToken));
//            // 쿠키에서 Access Token 제거
//            removeCookie(response, "Access-Token");
//        }
//
//        // 나머지 로그아웃 로직
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("userDetails = " + userDetails);
        String accessToken = extractAccessTokenFromCookie(request);
        blackListService.addToBlacklist(accessToken);
        tokenService.deleteRefreshToken(userDetails.getUsername());
        removeCookie(response, "Access-Token");
        return ResponseEntity.noContent().build();
    }

    private String extractAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Access-Token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
