package com.ssafy.alttab.security.oauth2.handler;

import com.ssafy.alttab.config.UrlProperties;
import com.ssafy.alttab.security.jwt.JWTUtil;
import com.ssafy.alttab.security.oauth2.dto.CustomOAuth2User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ssafy.alttab.security.jwt.TokenExpireTime.ACCESS_TOKEN_EXPIRE_TIME;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final UrlProperties urlProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        System.out.println("customOAuth2User.getAttributes() = " + customOAuth2User.getAttributes());

        String username = customOAuth2User.getUsername();

        String accessToken = jwtUtil.generateAccessToken(authentication, username);
        jwtUtil.generateRefreshToken(authentication, accessToken, username);

        response.addCookie(createCookie("Access-Token", accessToken));
//        response.sendRedirect("http://localhost:3000/");
        response.sendRedirect(urlProperties.getFront());
    }

    static public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge((int) ACCESS_TOKEN_EXPIRE_TIME);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        return cookie;
    }
}
