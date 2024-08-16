package com.ssafy.alttab.common.security.handler;

import com.ssafy.alttab.common.util.JwtUtil;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Value("${app.front.url}")
    private String redirectUri;

    /**
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Member member = processOAuth2User(oAuth2User);

        String accessToken = jwtUtil.generateToken(member.getName());
        String refreshToken = jwtUtil.generateRefreshToken(member.getName());

        jwtUtil.saveRefreshToken(member.getName(), refreshToken);

        setTokenCookies(response, accessToken, refreshToken);

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

    /**
     * @param oAuth2User
     * @return
     */
    private Member processOAuth2User(OAuth2User oAuth2User) {
        String name = oAuth2User.getAttribute("login");
        String avatarUrl = oAuth2User.getAttribute("avatar_url");
        return memberService.saveOrUpdateMember(name, avatarUrl);
    }

    //== build ==//

    /**
     * @param response
     * @param accessToken
     * @param refreshToken
     */
    private void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessTokenCookie = createCookie("access_token", accessToken);
        ResponseCookie refreshTokenCookie = createCookie("refresh_token", refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    /**
     * @param name
     * @param value
     * @return
     */
    private ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .build();
    }
}