package com.ssafy.alt_tab.jwt;

import com.ssafy.alt_tab.member.dto.CustomOAuth2User;
import com.ssafy.alt_tab.member.dto.MemberDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.ssafy.alt_tab.oauth2.OAuth2SuccessHandler.createCookie;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //cookie들을 불러온 뒤 Access-Token Key에 담긴 쿠키를 찾음
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Access-Token")) {
                accessToken = cookie.getValue();
            }
        }

        //Authorization 헤더 검증
        if (accessToken == null) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }


        //토큰
        String token = accessToken;
        if(jwtUtil.isBlacklisted(token)){
            System.out.println("blacklisted");
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.validateToken(accessToken)) { // 만료됐을때
            String reissueAccessToken = jwtUtil.reissueAccessToken(accessToken);
            if (StringUtils.hasText(reissueAccessToken)) {
                response.addCookie(createCookie("Access-Token", reissueAccessToken));
                token = reissueAccessToken;
            }
            System.out.println("재발급함~~~~~~~");
        } else {
            jwtUtil.reissueRefreshToken(accessToken);
            System.out.println("refresh재발급~~~~~~~~~~~");
        }

        //토큰에서 username & role 획득
        String username = jwtUtil.getUsername(token);
        String name = jwtUtil.getName(token);
        String role = jwtUtil.getRole(token);

        //userDTO를 생성하여 값 set
        MemberDto memberDto = new MemberDto();
        memberDto.setName(name);
        memberDto.setUsername(username);
        memberDto.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDto);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
