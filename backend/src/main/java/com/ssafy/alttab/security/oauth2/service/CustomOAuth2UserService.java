package com.ssafy.alttab.security.oauth2.service;

import com.ssafy.alttab.security.oauth2.dto.CustomOAuth2User;
import com.ssafy.alttab.security.oauth2.dto.GithubResponse;
import com.ssafy.alttab.member.dto.MemberDto;
import com.ssafy.alttab.security.oauth2.dto.OAuth2Response;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        System.out.println("oAuth2User = " + oAuth2User);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response;
        if (registrationId.equals("github")) {
            oAuth2Response = new GithubResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        Member member = memberRepository.findByUsername(username);
        System.out.println("exist Data: " + member);

        if (member != null) {
            member.setMemberName(oAuth2Response.getName());
            member.setMemberEmail(oAuth2Response.getEmail());
            member.setMemberAvatarUrl(oAuth2Response.getAvatarUrl());
            member.setMemberHtmlUrl(oAuth2Response.getHtmlUrl());

            memberRepository.save(member);

            MemberDto memberDto = new MemberDto();
            memberDto.setUsername(member.getUsername());
            memberDto.setMemberName(oAuth2Response.getName());
            memberDto.setMemberEmail(oAuth2Response.getEmail());
            memberDto.setMemberAvatarUrl(oAuth2Response.getAvatarUrl());
            memberDto.setMemberHtmlUrl(oAuth2Response.getHtmlUrl());
            memberDto.setRole("ROLE_USER");

            return new CustomOAuth2User(memberDto);
        } else {
            member = new Member();
            member.setUsername(username);
            member.setMemberName(oAuth2Response.getName());
            member.setMemberEmail(oAuth2Response.getEmail());
            member.setMemberAvatarUrl(oAuth2Response.getAvatarUrl());
            member.setMemberHtmlUrl(oAuth2Response.getHtmlUrl());
            member.setRole("ROLE_USER");

            memberRepository.save(member);

            MemberDto memberDto = new MemberDto();
            memberDto.setUsername(username);
            memberDto.setMemberName(oAuth2Response.getName());
            memberDto.setMemberEmail(oAuth2Response.getEmail());
            memberDto.setMemberAvatarUrl(oAuth2Response.getAvatarUrl());
            memberDto.setMemberHtmlUrl(oAuth2Response.getHtmlUrl());
            memberDto.setRole("ROLE_USER");

            return new CustomOAuth2User(memberDto);
        }
    }
}
