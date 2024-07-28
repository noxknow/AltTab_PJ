package com.ssafy.alt_tab.redis.service;

import com.ssafy.alt_tab.redis.entity.BlackList;
import com.ssafy.alt_tab.redis.repository.BlackListRepository;
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
