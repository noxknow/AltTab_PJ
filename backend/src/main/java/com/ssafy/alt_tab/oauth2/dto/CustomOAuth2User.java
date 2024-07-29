package com.ssafy.alt_tab.oauth2.dto;

import com.ssafy.alt_tab.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User, UserDetails { // OAuth2UserDetail

    private final MemberDto memberDto;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) memberDto::getRole);
        return collection;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getName() {
        return memberDto.getName();
    }

    public String getUsername() {
        return memberDto.getUsername();
    }
}
