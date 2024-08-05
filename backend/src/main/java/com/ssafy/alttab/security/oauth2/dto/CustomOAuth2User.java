package com.ssafy.alttab.security.oauth2.dto;

import com.ssafy.alttab.member.dto.MemberResponseDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User, UserDetails { // OAuth2UserDetail

    private final MemberResponseDto memberDto;

    public CustomOAuth2User(final MemberResponseDto memberDto) {
        this.memberDto = memberDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> String.valueOf(memberDto.getRole()));
        return collection;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getName() {
        return memberDto.getMemberName();
    }

    public String getUsername() {
        return memberDto.getUsername();
    }
}
