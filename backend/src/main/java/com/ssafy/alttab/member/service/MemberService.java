package com.ssafy.alttab.member.service;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.member.dto.MemberInfoResponseDto;
import com.ssafy.alttab.member.dto.MemberJoinedStudiesResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.member.repository.MemberRepository;

import java.util.stream.Collectors;

import com.ssafy.alttab.study.dto.JoinedStudyResponseDto;
import com.ssafy.alttab.study.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.alttab.member.entity.Member.createMember;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
                .name(member.getName())
                .avatarUrl(member.getAvatarUrl())
                .build();
    }

    public MemberJoinedStudiesResponseDto getJoinedStudies(String name) throws MemberNotFoundException {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new MemberNotFoundException(name));

        return MemberJoinedStudiesResponseDto.builder()
                .joinedStudies(member.getMemberStudies().stream()
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
}
