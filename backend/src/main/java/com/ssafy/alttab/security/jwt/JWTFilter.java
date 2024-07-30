package com.ssafy.alttab.security.jwt;

import com.ssafy.alttab.security.oauth2.dto.CustomOAuth2User;
import com.ssafy.alttab.member.dto.MemberDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.ssafy.alttab.security.jwt.JWTUtil.findCookie;
import static com.ssafy.alttab.security.oauth2.handler.OAuth2SuccessHandler.createCookie;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // cookie 불러온 뒤 Access-Token Key 담긴 쿠키를 찾음
        String accessToken = findCookie("Access-Token", request);

        //  검증
        if (accessToken == null) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        if (jwtUtil.isBlacklisted(accessToken)) {
            System.out.println("token blacklisted");
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.validateToken(accessToken)) { // 만료
            String reissueAccessToken = jwtUtil.reissueAccessToken(accessToken);
            if (StringUtils.hasText(reissueAccessToken)) {
                response.addCookie(createCookie("Access-Token", reissueAccessToken));
                accessToken = reissueAccessToken;
            }
            System.out.println("reissueAccessToken@@@@@@@@@@@@@");
        } else {
            jwtUtil.reissueRefreshToken(accessToken);
        }

        //토큰에서 username & role 획득
        String username = jwtUtil.getUsername(accessToken);
        String name = jwtUtil.getName(accessToken);
        String role = jwtUtil.getRole(accessToken);

        MemberDto memberDto = new MemberDto();
        memberDto.setName(name);
        memberDto.setUsername(username);
        memberDto.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDto);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
