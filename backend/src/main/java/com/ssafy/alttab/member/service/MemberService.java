package com.ssafy.alttab.member.service;

import com.ssafy.alttab.common.exception.ResourceNotFoundException;
import com.ssafy.alttab.member.dto.MemberDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ssafy.alttab.common.enums.ErrorCode.INVALID_TOKEN;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto getMemberByUsername(String username) {
        return convertToDto(memberRepository.findByUsername(username));
    }


    public boolean updateMember(String username, MemberDto memberDto) {
        Member member = memberRepository.findByUsername(username);
//                .orElseThrow(() -> new ResourceNotFoundException(INVALID_TOKEN, "Member not found"));

        if (member != null) {

            member.setUsername(memberDto.getUsername());
            member.setMemberName(memberDto.getMemberName());
            member.setMemberEmail(memberDto.getMemberEmail());
            member.setMemberAvatarUrl(memberDto.getMemberAvatarUrl());
            member.setMemberHtmlUrl(memberDto.getMemberHtmlUrl());
            member.setRole(memberDto.getRole());

            memberRepository.save(member);
//        return convertToDto(member);
            return true;
        } else return false;
    }

    public boolean deleteMemberAndLogout(String username, String accessToken) {
        Member member = memberRepository.findByUsername(username);

        if (member != null) {
            // Delete member
            memberRepository.delete(member);

            // Blacklist token and delete refresh token


            return true;
        } else {
            throw new ResourceNotFoundException(INVALID_TOKEN,"Member not found with username: " + username);
        }
    }

    private MemberDto convertToDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(member.getUsername());
        memberDto.setMemberName(member.getUsername());
        memberDto.setMemberEmail(member.getMemberName());
        memberDto.setMemberAvatarUrl(member.getMemberAvatarUrl());
        memberDto.setMemberHtmlUrl(member.getMemberHtmlUrl());
        memberDto.setRole(member.getRole());
        return memberDto;
    }
}
