package com.ssafy.alttab.member.service;

import com.ssafy.alttab.member.dto.MemberDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto getMemberByUsername(String username) {
        Member member = findByUsernameOrThrow(username);
        return MemberDto.fromEntity(member);
    }

    @Transactional
    public boolean updateMember(String username, MemberDto memberDto) {
        Member member = findByUsernameOrThrow(username);
        if (member != null) {
            member.setMemberName(memberDto.getMemberName());
            member.setMemberEmail(memberDto.getMemberEmail());
            member.setMemberAvatarUrl(memberDto.getMemberAvatarUrl());
            member.setMemberHtmlUrl(memberDto.getMemberHtmlUrl());
            System.out.println("member = " + member);
            memberRepository.save(member);
            return true;
        } else return false;
    }

    public void deleteMember(String username) {
        Member member = findByUsernameOrThrow(username);
        if (member != null) {
            memberRepository.delete(member);
        }
    }

    private Member findByUsernameOrThrow(String username) {
        return memberRepository.findByUsername(username)
                .orElse(null);
    }
}
