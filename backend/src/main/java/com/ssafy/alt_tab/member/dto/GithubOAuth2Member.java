package com.ssafy.alt_tab.member.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class GithubOAuth2Member implements OAuth2User {

    private final MemberDto memberDto;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberDto.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return "";
    }

    public String getUsername(){
        return memberDto.getUsername();
    }
}
