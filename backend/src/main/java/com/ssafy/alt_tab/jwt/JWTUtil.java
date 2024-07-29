package com.ssafy.alt_tab.jwt;

import com.ssafy.alt_tab.oauth2.exception.TokenException;
import com.ssafy.alt_tab.redis.entity.Token;
import com.ssafy.alt_tab.redis.service.BlackListService;
import com.ssafy.alt_tab.redis.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.alt_tab.exception.ErrorCode.INVALID_JWT_SIGNATURE;
import static com.ssafy.alt_tab.exception.ErrorCode.INVALID_TOKEN;
import static com.ssafy.alt_tab.jwt.TokenExpireTime.ACCESS_TOKEN_EXPIRE_TIME;
import static com.ssafy.alt_tab.jwt.TokenExpireTime.REFRESH_TOKEN_EXPIRE_TIME;

@Component
public class JWTUtil { // JWT 발급 & 검증

    private final SecretKey secretKey;
    private final TokenService tokenService;
    private final BlackListService blackListService;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret, TokenService tokenService, BlackListService blackListService) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.tokenService = tokenService;
        this.blackListService = blackListService;
    }

    public String getName(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String generateAccessToken(Authentication authentication, String username) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME, username);
    }

    public void generateRefreshToken(Authentication authentication, String accessToken, String username) {
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME, username);
        tokenService.saveOrUpdate(username, refreshToken, accessToken);
    }

    private String generateToken(Authentication authentication, long expireTime, String username) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("username", username)
                .claim("role", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime * 1000L))
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {

        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get("role").toString()));
    }

    public String reissueAccessToken(String accessToken) {

        if (StringUtils.hasText(accessToken)) {
            Token token = tokenService.findByAccessTokenOrThrow(accessToken);
            String refreshToken = token.getRefreshToken();
            if (validateToken(refreshToken)) {
                String reissueAccessToken = generateAccessToken(getAuthentication(refreshToken), getUsername(refreshToken));
                tokenService.updateAccessToken(reissueAccessToken, token);
                return reissueAccessToken;
            }
        }
        return null;
    }

    public void reissueRefreshToken(String accessToken) {
        if (StringUtils.hasText(accessToken)) {
            try {
                tokenService.findByAccessTokenOrThrow(accessToken);
            } catch (TokenException e) {
                // 토큰이 없으면 새 리프레시 토큰 생성 및 저장
                String username = getUsername(accessToken);
                Authentication authentication = getAuthentication(accessToken);
                String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME, username);
                tokenService.saveOrUpdate(username, refreshToken, accessToken);
                System.out.println("reissueRefreshToken@@@@@@@@@@@@@");
            }
        }
    }

    public boolean validateToken(String token) {

        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    private Claims parseClaims(String token) {

        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (MalformedJwtException e) {
            throw new TokenException(INVALID_TOKEN);
        } catch (SecurityException e) {
            throw new TokenException(INVALID_JWT_SIGNATURE);
        }
    }

    public boolean isBlacklisted(String token) {
        return blackListService.isBlacklisted(token);
    }

    public static String findCookie(String name, HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
