package com.ssafy.alttab.common.security.service;

import com.ssafy.alttab.common.exception.TokenNotValidException;
import com.ssafy.alttab.common.exception.TokenNotFoundException;
import com.ssafy.alttab.common.util.CookieUtil;
import com.ssafy.alttab.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;

    /**
     * HTTP 요청에서 리프레시 토큰을 추출하고, 새로운 리프레시 토큰을 생성하여 ResponseCookie로 반환
     *
     * @param request 클라이언트의 HTTP 요청
     * @return 새로 생성된 리프레시 토큰이 포함된 ResponseCookie
     * @throws TokenNotFoundException 요청에 리프레시 토큰이 없는 경우
     * @throws TokenNotValidException 리프레시 토큰이 유효하지 않은 경우
     */
    public ResponseCookie generateRefreshToken(HttpServletRequest request) {
        String refreshToken = getRefreshTokenFromRequest(request);
        String username = validateAndExtractUsername(refreshToken);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);
        return createRefreshTokenCookie(newRefreshToken);
    }

    /**
     * HTTP 요청에서 리프레시 토큰을 추출
     *
     * @param request 클라이언트의 HTTP 요청
     * @return 추출된 리프레시 토큰
     * @throws TokenNotFoundException 요청에 리프레시 토큰이 없는 경우
     */
    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        String refreshToken = CookieUtil.getCookieValue(request, "refresh_token");
        if (refreshToken == null) {
            throw new TokenNotFoundException("refresh Token");
        }
        return refreshToken;
    }

    /**
     * 리프레시 토큰의 유효성을 검사하고 사용자 이름을 추출
     *
     * @param refreshToken 검증할 리프레시 토큰
     * @return 토큰에서 추출한 사용자 이름
     * @throws TokenNotValidException 토큰이 유효하지 않거나 사용자 이름을 추출할 수 없는 경우
     */
    private String validateAndExtractUsername(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        if (username == null || !jwtUtil.validateRefreshToken(refreshToken, username)) {
            throw new TokenNotValidException("refresh Token");
        }
        return username;
    }

    //== builder ==//

    /**
     * 새로운 리프레시 토큰으로 ResponseCookie를 생성
     *
     * @param newRefreshToken 새로 생성된 리프레시 토큰
     * @return 생성된 ResponseCookie
     */
    private ResponseCookie createRefreshTokenCookie(String newRefreshToken) {
        return ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(jwtUtil.extractExpiration(newRefreshToken).getTime())
                .path("/")
                .build();
    }
}