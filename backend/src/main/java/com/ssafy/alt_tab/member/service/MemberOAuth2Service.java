package com.ssafy.alt_tab.member.service;

import com.ssafy.alt_tab.member.dto.GithubOAuth2Member;
import com.ssafy.alt_tab.member.dto.GithubResponse;
import com.ssafy.alt_tab.member.dto.MemberDto;
import com.ssafy.alt_tab.member.dto.OAuth2Response;
import com.ssafy.alt_tab.member.entity.MemberEntity;
import com.ssafy.alt_tab.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberOAuth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        System.out.println("oAuth2User = " + oAuth2User);
        System.out.println("oAuth2UserRequest.getClientRegistration() = " + oAuth2UserRequest.getClientRegistration());

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        System.out.println("registrationId = " + registrationId);
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("github")) {
            oAuth2Response = new GithubResponse(oAuth2User.getAttributes());
            System.out.println("oAuth2Response = " + oAuth2Response);
        } else {
            return null;
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("attributes = " + attributes);

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        MemberEntity existData = memberRepository.findByUsername(username);
        System.out.println("exist Data: " + existData);

        if (existData == null) {
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setUsername(username);
            memberEntity.setEmail(oAuth2Response.getEmail());
            memberEntity.setName(oAuth2Response.getName());
            memberEntity.setRole("ROLE_USER");

            memberRepository.save(memberEntity);

            MemberDto memberDto = new MemberDto();
            memberDto.setUsername(username);
            memberDto.setName(oAuth2Response.getName());
            memberDto.setRole("ROLE_USER");

            return new GithubOAuth2Member(memberDto);
        } else {
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            memberRepository.save(existData);

            MemberDto memberDto = new MemberDto();
            memberDto.setUsername(existData.getUsername());
            memberDto.setName(oAuth2Response.getName());
            memberDto.setRole(existData.getRole());

            return new GithubOAuth2Member(memberDto);
        }
    }
}
