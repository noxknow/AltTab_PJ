package com.ssafy.alttab.community.service;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.common.jointable.repository.MemberStudyRepository;
import com.ssafy.alttab.community.dto.*;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.study.entity.Study;
import com.ssafy.alttab.study.repository.StudyRepository;
import jakarta.transaction.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
public class CommunityService {

    private static final int TOP_LIMIT = 6;
    private final StudyRepository studyRepository;
    private final MemberStudyRepository memberStudyRepository;
    private final MemberRepository memberRepository;

    /**
     * 커뮤니티 메인 페이지에 필요한 데이터를 조회
     *
     * @return CommunityMainResponseDto 커뮤니티 메인 페이지 데이터
     */
    @Transactional
    public CommunityMainResponseDto getCommunityMain(String name) throws MemberNotFoundException {
        return CommunityMainResponseDto.builder()
                .weeklyStudies(getWeeklyStudies(name))
                .topSolvers(getTopSolversStudyList(name))
                .build();
    }

    @Transactional
    public AfterFollowDto followStudy(String username, Long studyId) throws MemberNotFoundException, StudyNotFoundException {
        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new MemberNotFoundException(username));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));

        Optional<MemberStudy> optionalMemberStudy = memberStudyRepository.findByMemberAndStudyAndRole(member, study, MemberRoleStatus.FOLLOWER);

        if (optionalMemberStudy.isPresent()) {
            MemberStudy memberStudy = optionalMemberStudy.get();
            member.removeMemberStudy(memberStudy);
            study.removeMemberStudy(memberStudy);
            memberStudyRepository.delete(memberStudy);
            return AfterFollowDto.builder()
                    .check(false)
                    .like(study.getFollowerCount())
                    .build();
        } else {
            MemberStudy memberStudy = MemberStudy
                    .createMemberStudy(member, study, MemberRoleStatus.FOLLOWER);
            member.getMemberStudies().add(memberStudy);
            study.getMemberStudies().add(memberStudy);
            memberStudyRepository.save(memberStudy);
            return AfterFollowDto.builder()
                    .check(true)
                    .like(study.getFollowerCount())
                    .build();
        }
    }

    /**
     * 주간 인기 스터디 목록을 조회
     *
     * @return List<WeeklyStudyDto> 주간 인기 스터디 목록
     */
    @Transactional
    public List<WeeklyStudyDto> getWeeklyStudies(String name) throws MemberNotFoundException {
        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(6);
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new MemberNotFoundException(name));
        return studyRepository.findByCreatedAtBetween(startOfWeek, endOfWeek)
                .stream()
                .sorted(Comparator.comparing(Study::getFollowerCount, Comparator.reverseOrder()))
                .limit(TOP_LIMIT)
                .map(study -> mapToWeeklyStudyDto(member, study))
                .collect(Collectors.toList());
    }

    /**
     * @param study
     * @return
     */
    private LeaderMemberDto getLeaderInfo(Study study) {
        return memberStudyRepository.findMemberByStudyIdAndRole(study.getId(), MemberRoleStatus.LEADER)
                .map(leader -> LeaderMemberDto.builder()
                        .name(leader.getMember().getName())
                        .avatarUrl(leader.getMember().getAvatarUrl())
                        .build())
                .orElse(null);
    }

    /**
     * 상위 문제 해결자 목록을 조회
     *
     * @return List<TopSolverDto> 상위 문제 해결자 목록
     */
    @Transactional
    public List<TopSolverDto> getTopSolversStudyList(String username) throws MemberNotFoundException {
        Member member = memberRepository.findByName(username).orElseThrow(() -> new MemberNotFoundException(username));
        return studyRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Study::totalSolve, Comparator.reverseOrder()))
                .limit(TOP_LIMIT)
                .map(study -> mapToTopSolverDto(member, study))
                .collect(Collectors.toList());
    }

    /**
     * 팔로워 수가 많은 상위 스터디 목록을 조회
     *
     * @return List<TopFollowerDto> 팔로워 수 기준 상위 스터디 목록
     */
    @Transactional
    public List<TopFollowerDto> getTopFollowerStudyList(String username) throws MemberNotFoundException {
        Member member = memberRepository.findByName(username).orElseThrow(() -> new MemberNotFoundException(username));
        return studyRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Study::getFollowerCount, Comparator.reverseOrder()))
                .limit(TOP_LIMIT)
                .map(study -> mapToTopFollowerDto(member, study))
                .collect(Collectors.toList());
    }
    @Transactional
    public List<FollowingStudyDto> getFollowingStudy(String username) {
        List<MemberStudy> memberStudies = memberStudyRepository.findByMemberName(username);
        return memberStudies.stream()
                .filter(memberStudy -> memberStudy.getRole().equals(MemberRoleStatus.FOLLOWER))
                .map(memberStudy -> mapToFollowingStudyDto(memberStudy.getMember(), memberStudy.getStudy()))
                .collect(Collectors.toList());
    }

    //==map dto==//

    /**
     * StudyInfo 엔티티를 WeeklyStudyDto로 변환
     *
     * @param study StudyInfo 엔티티
     * @return WeeklyStudyDto 변환된 DTO 객체
     */
    private WeeklyStudyDto mapToWeeklyStudyDto(Member member, Study study) {
        return WeeklyStudyDto.builder()
                .studyId(study.getId())
                .studyName(study.getStudyName())
                .studyDescription(study.getStudyDescription())
                .like(study.getFollowerCount())
                .totalSolve(study.totalSolve())
                .leaderMemberDto(getLeaderInfo(study))
                .check(memberStudyRepository.findByMemberAndStudyAndRole(member, study, MemberRoleStatus.FOLLOWER).isPresent())
                .build();
    }

    /**
     * StudyInfo 엔티티를 TopFollowerDto로 변환
     *
     * @param study StudyInfo 엔티티
     * @return TopFollowerDto 변환된 DTO 객체
     */
    private TopFollowerDto mapToTopFollowerDto(Member member, Study study) {
        return TopFollowerDto.builder()
                .studyId(study.getId())
                .studyName(study.getStudyName())
                .studyDescription(study.getStudyDescription())
                .like(study.getFollowerCount())
                .totalSolve(study.totalSolve())
                .leaderMemberDto(getLeaderInfo(study))
                .check(memberStudyRepository.findByMemberAndStudyAndRole(member, study, MemberRoleStatus.FOLLOWER).isPresent())
                .build();
    }

    /**
     * StudyInfo 엔티티를 TopSolverDto로 변환
     *
     * @param study StudyInfo 엔티티
     * @return TopSolverDto 변환된 DTO 객체
     */
    private TopSolverDto mapToTopSolverDto(Member member, Study study) {
        return TopSolverDto.builder()
                .studyId(study.getId())
                .studyName(study.getStudyName())
                .studyDescription(study.getStudyDescription())
                .like(study.getFollowerCount())
                .totalSolve(study.totalSolve())
                .leaderMemberDto(getLeaderInfo(study))
                .check(memberStudyRepository.findByMemberAndStudyAndRole(member, study, MemberRoleStatus.FOLLOWER).isPresent())
                .build();
    }

    private FollowingStudyDto mapToFollowingStudyDto(Member member, Study study) {
        return FollowingStudyDto.builder()
                .studyId(study.getId())
                .studyName(study.getStudyName())
                .studyDescription(study.getStudyDescription())
                .like(study.getFollowerCount())
                .totalSolve(study.totalSolve())
                .leaderMemberDto(getLeaderInfo(study))
                .check(memberStudyRepository.findByMemberAndStudyAndRole(member, study, MemberRoleStatus.FOLLOWER).isPresent())
                .build();
    }

}
