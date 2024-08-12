package com.ssafy.alttab.community.service;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
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
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ssafy.alttab.member.enums.MemberRoleStatus.FOLLOWER;

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
    public CommunityMainResponseDto getCommunityMain() {
        return CommunityMainResponseDto.builder()
                .weeklyStudies(getWeeklyStudies())
                .topSolvers(getTopSolvers())
                .build();
    }

    /**
     * 주간 인기 스터디 목록을 조회
     *
     * @return List<WeeklyStudyDto> 주간 인기 스터디 목록
     */
    @Transactional
    public List<WeeklyStudyDto> getWeeklyStudies() {
        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(6);

        return studyRepository.findByCreatedAtBetween(startOfWeek, endOfWeek)
                .stream()
                .sorted(Comparator.comparing(Study::getLike, Comparator.reverseOrder())
                        .thenComparing(Study::getView, Comparator.reverseOrder()))
                .limit(TOP_LIMIT)
                .map(this::mapToWeeklyStudyDto)
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
    public List<TopSolverDto> getTopSolvers() {
        return studyRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Study::totalSolve, Comparator.reverseOrder()))
                .limit(TOP_LIMIT)
                .map(this::mapToTopSolverDto)
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
//                .map(this::mapToTopFollowerDto)
                .map(study -> mapToTopFollowerDto(member, study))
                .collect(Collectors.toList());
    }

    //==map dto==//

    /**
     * StudyInfo 엔티티를 WeeklyStudyDto로 변환
     *
     * @param study StudyInfo 엔티티
     * @return WeeklyStudyDto 변환된 DTO 객체
     */
    private WeeklyStudyDto mapToWeeklyStudyDto(Study study) {
        return WeeklyStudyDto.builder()
                .name(study.getStudyName())
                .studyId(study.getId())
                .like(study.getLike())
                .studyDescription(study.getStudyDescription())
                .follower(study.getFollowerCount())
                .view(study.getView())
                .leaderMemberDto(getLeaderInfo(study))
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
                .name(study.getStudyName())
                .like(study.getLike())
                .studyDescription(study.getStudyDescription())
                .totalFollower(study.getFollowerCount())  // 팔로워 수로 변경
                .view(study.getView())
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
    private TopSolverDto mapToTopSolverDto(Study study) {
        return TopSolverDto.builder()
                .name(study.getStudyName())
                .studyId(study.getId())
                .like(study.getLike())
                .studyDescription(study.getStudyDescription())
                .totalSolve(study.totalSolve())
                .view(study.getView())
                .leaderMemberDto(getLeaderInfo(study))
                .build();
    }
}