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
        Member existData = memberRepository.findByUsername(username);
        System.out.println("exist Data: " + existData);

        if (existData == null) {
            Member member = new Member();
            member.setUsername(username);
            member.setName(oAuth2Response.getName());
            member.setEmail(oAuth2Response.getEmail());
            member.setAvatar_url(oAuth2Response.getAvatarUrl());
            member.setHtml_url(oAuth2Response.getHtmlUrl());
            member.setRole("ROLE_USER");

            memberRepository.save(member);

            MemberDto memberDto = new MemberDto();
            memberDto.setUsername(username);
            memberDto.setName(oAuth2Response.getName());
//            memberDto.setAvatar_url(oAuth2Response.getAvatarUrl());
//            memberDto.setHtml_url(oAuth2Response.getHtmlUrl());
            memberDto.setRole("ROLE_USER");

            return new CustomOAuth2User(memberDto);
        } else {
            existData.setName(oAuth2Response.getName());
            existData.setEmail(oAuth2Response.getEmail());
            existData.setAvatar_url(oAuth2Response.getAvatarUrl());
            existData.setHtml_url(oAuth2Response.getHtmlUrl());

            memberRepository.save(existData);

            MemberDto memberDto = new MemberDto();
            memberDto.setUsername(existData.getUsername());
            memberDto.setName(oAuth2Response.getName());
//            memberDto.setAvatar_url(oAuth2Response.getAvatarUrl());
//            memberDto.setHtml_url(oAuth2Response.getHtmlUrl());
            memberDto.setRole(existData.getRole());

            return new CustomOAuth2User(memberDto);
        }
    }
}
