package com.ssafy.alttab.community.service;

import com.ssafy.alttab.community.dto.CommunityMainResponseDto;
import com.ssafy.alttab.community.dto.TopFollowerDto;
import com.ssafy.alttab.community.dto.TopSolverDto;
import com.ssafy.alttab.community.dto.WeeklyStudyDto;
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

@Service
@RequiredArgsConstructor
@Builder
public class CommunityService {

    private static final int TOP_LIMIT = 10;
    private final StudyRepository studyRepository;

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
                .follower(study.getFollowerCount())
                .view(study.getView())
                .build();
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
    public List<TopFollowerDto> getTopFollowerStudys() {
        return studyRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Study::getFollowerCount, Comparator.reverseOrder()))
                .limit(TOP_LIMIT)
                .map(this::mapToTopFollowerDto)
                .collect(Collectors.toList());
    }

    //==map dto==//

    /**
     * StudyInfo 엔티티를 TopFollowerDto로 변환
     *
     * @param study StudyInfo 엔티티
     * @return TopFollowerDto 변환된 DTO 객체
     */
    private TopFollowerDto mapToTopFollowerDto(Study study) {
        return TopFollowerDto.toDto(study);
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
                .totalSolve(study.totalSolve())
                .view(study.getView())
                .build();
    }
}