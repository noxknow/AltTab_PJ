package com.ssafy.alttab.common.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // 여기서는 실제 데이터베이스 조회 대신 임시로 사용자를 생성합니다.
    // 실제 구현에서는 데이터베이스에서 사용자 정보를 조회해야 합니다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 예시: 실제로는 데이터베이스에서 사용자 정보를 조회해야 합니다.
        return new User(username, "", new ArrayList<>());
    }
}