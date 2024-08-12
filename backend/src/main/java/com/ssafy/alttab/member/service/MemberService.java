package com.ssafy.alttab.member.service;

import static com.ssafy.alttab.member.entity.Member.createMember;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.common.jointable.repository.MemberStudyRepository;
import com.ssafy.alttab.member.dto.MemberInfoResponseDto;
import com.ssafy.alttab.member.dto.MemberJoinedStudiesResponseDto;
import com.ssafy.alttab.member.dto.MemberListResponseDto;
import com.ssafy.alttab.member.dto.MemberSearchResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.study.dto.FollowStudyListResponseDto;
import com.ssafy.alttab.study.dto.FollowStudyResponseDto;
import com.ssafy.alttab.study.dto.JoinedStudyResponseDto;
import com.ssafy.alttab.study.entity.Study;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ssafy.alttab.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
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
                .build();
    }

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
        List<Member> members = memberRepository.findByNameStartingWith(name);
        return MemberListResponseDto.builder()
                .members(members.stream()
                        .map(MemberSearchResponseDto::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void follow(String username, Long studyId) throws MemberNotFoundException, StudyNotFoundException {
        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));

        Optional<MemberStudy> optionalMemberStudy = memberStudyRepository.findByMemberAndStudyAndRole(member, study, MemberRoleStatus.FOLLOWER);

        if (optionalMemberStudy.isPresent()) {
            MemberStudy memberStudy = optionalMemberStudy.get();
            member.removeMemberStudy(memberStudy);
            study.removeMemberStudy(memberStudy);
            memberStudyRepository.delete(memberStudy);
        } else {
            MemberStudy memberStudy = MemberStudy
                    .createMemberStudy(member, study, MemberRoleStatus.FOLLOWER);
            member.getMemberStudies().add(memberStudy);
            study.getMemberStudies().add(memberStudy);
            memberStudyRepository.save(memberStudy);
        }
    }

    public FollowStudyListResponseDto getFollowStudyList(String username) {
        List<MemberStudy> memberStudies = memberStudyRepository.findByMemberName(username);
        return FollowStudyListResponseDto.builder()
                .followStudyList(
                        memberStudies.stream()
                                .filter(memberStudy -> memberStudy.getRole().equals(MemberRoleStatus.FOLLOWER))
                                .map(memberStudy -> FollowStudyResponseDto.toDto(memberStudy.getStudy()))
                                .collect(Collectors.toList())
                )
                .build();
    }
}