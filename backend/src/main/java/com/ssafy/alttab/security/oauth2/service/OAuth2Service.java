package com.ssafy.alttab.security.oauth2.service;

import com.ssafy.alttab.security.redis.entity.BlackList;
import com.ssafy.alttab.security.redis.repository.BlackListRepository;
import com.ssafy.alttab.security.redis.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ssafy.alttab.security.jwt.JWTUtil.findCookie;
import static com.ssafy.alttab.security.oauth2.controller.OAuth2Controller.removeCookie;

@Service
@RequiredArgsConstructor
public class OAuth2Service {
    private final TokenRepository tokenRepository;
    private final BlackListRepository blackListRepository;

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, String username) {
        String accessToken = findCookie("Access-Token", request);
        if (accessToken != null) {
            blackListRepository.save(new BlackList(accessToken));
        }
        tokenRepository.deleteById(username);
        removeCookie(response);
    }
}
