package com.ssafy.alttab.common.security;

import com.ssafy.alttab.common.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // GitHub에서 제공하는 사용자 정보를 확인
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // email이나 name이 null인 경우 대체값 사용
        String username = (email != null) ? email : "defaultEmail@example.com";

        // 사용자 정보로 UserDetails 객체 생성
        UserDetails userDetails = User.withUsername(username)
                .password("") // OAuth2 인증이므로 비밀번호는 빈 문자열로 설정
                .roles("USER") // 기본 역할 설정
                .build();

        String token = jwtUtil.generateToken(userDetails);
        String redirectUrl = "/loginSuccess?token=" + token;
        System.out.println("Redirecting to: " + redirectUrl);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}