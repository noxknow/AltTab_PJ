package com.ssafy.alttab.security.redis.service;

import com.ssafy.alttab.security.redis.entity.BlackList;
import com.ssafy.alttab.security.redis.repository.BlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;

    public void addToBlacklist(String token) {
        blackListRepository.save(new BlackList(token));
    }

    public boolean isBlacklisted(String token) {
        return blackListRepository.existsById(token);
    }
}