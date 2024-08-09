package com.ssafy.alttab.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.alttab.common.dto.ErrorResponseDto;
import com.ssafy.alttab.common.exception.TokenNotFoundException;
import com.ssafy.alttab.common.exception.TokenNotValidException;
import com.ssafy.alttab.common.util.CookieUtil;
import com.ssafy.alttab.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    /**
     * 내부 필터 메서드: 각 요청에 대해 JWT 인증을 처리
     *
     * @param request 현재 HTTP 요청
     * @param response 현재 HTTP 응답
     * @param chain 다음 필터로 요청을 전달하기 위한 필터 체인
     * @throws ServletException 서블릿 처리 중 발생할 수 있는 예외
     * @throws IOException 입출력 작업 중 발생할 수 있는 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String accessToken = CookieUtil.getCookieValue(request, "access_token");

            if (accessToken != null) {
                processToken(request, response, accessToken);
            }

            chain.doFilter(request, response);
        } catch (TokenNotFoundException | TokenNotValidException e) {
            handleException(response, e);
        }
    }

    /**
     * 액세스 토큰을 처리하고 필요한 경우 인증을 설정
     *
     * @param request 현재 HTTP 요청
     * @param response 현재 HTTP 응답
     * @param accessToken 처리할 JWT 액세스 토큰
     */
    private void processToken(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        String username = jwtUtil.extractUsername(accessToken);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(accessToken, userDetails)) {
                log.info("valid access token");
                setAuthentication(request, userDetails);
            } else {
                log.info("not valid access token");
                handleInvalidAccessToken(request, response, username, userDetails);
            }
        }
    }

    /**
     * 유효하지 않은 액세스 토큰을 처리합니다.
     * 리프레시 토큰이 유효한 경우 새로운 액세스 토큰을 생성하고, 그렇지 않으면 예외를 던집니다.
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param username 사용자 이름
     * @param userDetails 사용자 상세 정보
     * @throws TokenNotFoundException 리프레시 토큰이 없거나 유효하지 않은 경우
     */
    private void handleInvalidAccessToken(HttpServletRequest request, HttpServletResponse response, String username, UserDetails userDetails) {
        String refreshToken = CookieUtil.getCookieValue(request, "refresh_token");
        if (refreshToken == null) {
            log.error("not found refresh Token");
            throw new TokenNotFoundException("refresh Token");
        }

        if (!jwtUtil.validateRefreshToken(refreshToken, username)) {
            log.error("not valid refresh Token");
            throw new TokenNotValidException("refresh Token");
        }

        String newAccessToken = jwtUtil.generateToken(username);
        setNewAccessTokenCookie(response, newAccessToken);
        setAuthentication(request, userDetails);
    }

    //== build ==//

    /**
     * 현재 요청에 대한 인증을 설정
     *
     * @param request 현재 HTTP 요청
     * @param userDetails 인증에 사용될 사용자 상세 정보
     */
    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /**
     * 새로운 액세스 토큰을 쿠키로 설정
     *
     * @param response 현재 HTTP 응답
     * @param newAccessToken 설정할 새 액세스 토큰
     */
    private void setNewAccessTokenCookie(HttpServletResponse response, String newAccessToken) {
        ResponseCookie newAccessTokenCookie = ResponseCookie.from("access_token", newAccessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(jwtUtil.extractExpiration(newAccessToken).getTime())
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString());
    }

    /**
     * exception 시 httpResponse 에 401 실어서 보내기
     *
     * @param response not valid, not found
     * @param e TokenNotFoundException | TokenNotValidException
     * @throws IOException write 오류
     */
    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ErrorResponseDto errorResponse = new ErrorResponseDto(e);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}