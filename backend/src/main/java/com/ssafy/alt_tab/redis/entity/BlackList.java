package com.ssafy.alt_tab.redis.entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "blacklist", timeToLive = 60 * 60 * 24 * 7)
//@RedisHash(value = "blacklist", timeToLive = 20)
public class BlackList {
    @Id
    private String token;

}
