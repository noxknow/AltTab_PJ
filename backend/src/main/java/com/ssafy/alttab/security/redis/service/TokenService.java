package com.ssafy.alttab.security.redis.service;

import com.ssafy.alttab.common.exception.TokenException;
import com.ssafy.alttab.security.redis.entity.Token;
import com.ssafy.alttab.security.redis.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.alttab.common.enums.ErrorCode.TOKEN_EXPIRED;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void deleteRefreshToken(String username) {

        tokenRepository.deleteById(username);
    }

    @Transactional
    public void saveOrUpdate(String username, String refreshToken, String accessToken) {

        Token token = tokenRepository.findByAccessToken(accessToken)
                .map(o -> o.updateRefreshToken(refreshToken))
                .orElseGet(() -> new Token(username, refreshToken, accessToken));

        tokenRepository.save(token);
    }

    public Token findByAccessTokenOrThrow(String accessToken) {

        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(TOKEN_EXPIRED));
    }

    @Transactional
    public void updateAccessToken(String accessToken, Token token) {

        token.updateAccessToken(accessToken);
        tokenRepository.save(token);
    }
}