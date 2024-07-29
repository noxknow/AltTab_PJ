package com.ssafy.alt_tab.redis.entity;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import static com.ssafy.alt_tab.jwt.TokenExpireTime.REFRESH_TOKEN_EXPIRE_TIME;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "blacklist", timeToLive = REFRESH_TOKEN_EXPIRE_TIME)
public class BlackList {

    @Id
    private String token;
}
