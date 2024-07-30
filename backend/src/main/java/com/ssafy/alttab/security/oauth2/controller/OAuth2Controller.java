package com.ssafy.alttab.security.oauth2.controller;

import com.ssafy.alttab.security.redis.service.BlackListService;
import com.ssafy.alttab.security.redis.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;

import static com.ssafy.alttab.security.jwt.JWTUtil.findCookie;

@Controller
@RequiredArgsConstructor
public class OAuth2Controller {
    private final TokenService tokenService;
    private final BlackListService blackListService;

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) {

        String accessToken = findCookie("Access-Token", request);
        blackListService.addToBlacklist(accessToken);
        tokenService.deleteRefreshToken(userDetails.getUsername());
        removeCookie(response);
        return ResponseEntity.noContent().build();
    }

    private void removeCookie(HttpServletResponse response) {

        Cookie cookie = new Cookie("Access-Token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
