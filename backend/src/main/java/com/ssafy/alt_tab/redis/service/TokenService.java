package com.ssafy.alt_tab.redis.service;

import com.ssafy.alt_tab.oauth2.exception.TokenException;
import com.ssafy.alt_tab.redis.entity.Token;
import com.ssafy.alt_tab.redis.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.alt_tab.exception.ErrorCode.TOKEN_EXPIRED;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void deleteRefreshToken(String memberKey) {
        tokenRepository.deleteById(memberKey);
    }

    @Transactional
    public void saveOrUpdate(String memberKey, String refreshToken, String accessToken) {
        Token token = tokenRepository.findByAccessToken(accessToken)
                .map(o -> o.updateRefreshToken(refreshToken))
                .orElseGet(() -> new Token(memberKey, refreshToken, accessToken));

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

//    @Transactional
//    public void updateRefreshToken(String refreshToken, Token token) {
//        token.updateRefreshToken(refreshToken);
//        tokenRepository.save(token);
//    }

}
