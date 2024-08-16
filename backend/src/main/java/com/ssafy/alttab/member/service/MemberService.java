package com.ssafy.alttab.member.service;

import static com.ssafy.alttab.member.entity.Member.createMember;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.jointable.repository.MemberStudyRepository;
import com.ssafy.alttab.member.dto.MemberInfoResponseDto;
import com.ssafy.alttab.member.dto.MemberJoinedStudiesResponseDto;
import com.ssafy.alttab.member.dto.MemberListResponseDto;
import com.ssafy.alttab.member.dto.MemberSearchResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.study.dto.JoinedStudyResponseDto;
import com.ssafy.alttab.study.entity.Study;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberStudyRepository memberStudyRepository;

    @Transactional
    public Member saveOrUpdateMember(String name, String avatarUrl) {
        return memberRepository.findByName(name)
                .map(member -> {
                    member.changeAvatarUrl(avatarUrl);
                    return memberRepository.save(member);
                })
                .orElseGet(() ->
                        memberRepository.save(createMember(name, avatarUrl, MemberRoleStatus.MEMBER))
                );
    }

    @Transactional
    public MemberInfoResponseDto getMemberInfo(String name) throws MemberNotFoundException {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new MemberNotFoundException(name));
        return MemberInfoResponseDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .avatarUrl(member.getAvatarUrl())
                .memberPoint(member.getTotalPoint())
                .build();
    }

    @Transactional
    public MemberJoinedStudiesResponseDto getJoinedStudies(String name) throws MemberNotFoundException {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new MemberNotFoundException(name));
        return MemberJoinedStudiesResponseDto.builder()
                .joinedStudies(memberStudyRepository.findByMember(member)
                        .stream()
                        .filter(memberStudy -> memberStudy.getRole() != MemberRoleStatus.FOLLOWER)
                        .map(memberStudy -> {
                            Study study = memberStudy.getStudy();
                            return JoinedStudyResponseDto.builder()
                                    .studyId(study.getId())
                                    .studyName(study.getStudyName())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    public MemberListResponseDto searchMember(String name) {

        List<Member> members = memberRepository.findByNameContaining(name);

        return MemberListResponseDto.builder()
                .members(members.stream()
                        .map(MemberSearchResponseDto::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

}