package com.ssafy.alttab.redis.entity;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import static com.ssafy.alttab.jwt.TokenExpireTime.REFRESH_TOKEN_EXPIRE_TIME;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "jwt", timeToLive = REFRESH_TOKEN_EXPIRE_TIME)
public class Token {

    @Id
    private String username;

    private String refreshToken;

    @Indexed
    private String accessToken;

    public Token updateRefreshToken(String refreshToken) {

        this.refreshToken = refreshToken;
        return this;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}