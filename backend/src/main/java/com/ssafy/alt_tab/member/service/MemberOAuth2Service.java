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

@Service
@RequiredArgsConstructor
public class MemberOAuth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        System.out.println("oAuth2User = " + oAuth2User);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("github")) {
            oAuth2Response = new GithubResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }
        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        MemberEntity existData = memberRepository.findByUsername(username);

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

//        MemberDto memberDTO = new MemberDto();
//        memberDTO.setUsername(username);
//        memberDTO.setName(oAuth2Response.getName());
//        memberDTO.setRole("ROLE_USER");
//            return new GithubOAuth2Member(memberDTO);
    }

}
