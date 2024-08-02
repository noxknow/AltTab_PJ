package com.ssafy.alttab.community.service;

import com.ssafy.alttab.community.dto.CommunityMainResponseDto;
import com.ssafy.alttab.community.dto.TopSolverDto;
import com.ssafy.alttab.community.dto.WeeklyStudyDto;
import com.ssafy.alttab.study.entity.StudyInfo;
import com.ssafy.alttab.study.repository.StudyInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class CommunityService {

    private final StudyInfoRepository studyInfoRepository;
    private static final int TOP_LIMIT = 10;

    /**
     * 커뮤니티 메인 페이지에 필요한 데이터를 조회합니다.
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
     * 주간 인기 스터디 목록을 조회합니다.
     *
     * @return List<WeeklyStudyDto> 주간 인기 스터디 목록
     */
    @Transactional
    public List<WeeklyStudyDto> getWeeklyStudies() {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return studyInfoRepository.findByCreatedAtBetween(startOfWeek, endOfWeek)
                .stream()
                .sorted(Comparator.comparing(StudyInfo::getLike).reversed()
                        .thenComparing(StudyInfo::getView).reversed())
                .limit(TOP_LIMIT)
                .map(this::mapToWeeklyStudyDto)
                .collect(Collectors.toList());
    }

    /**
     * StudyInfo 엔티티를 WeeklyStudyDto로 변환합니다.
     *
     * @param studyInfo StudyInfo 엔티티
     * @return WeeklyStudyDto 변환된 DTO 객체
     */
    private WeeklyStudyDto mapToWeeklyStudyDto(StudyInfo studyInfo) {
        return WeeklyStudyDto.builder()
                .name(studyInfo.getStudyName())
                .like(formatLargeNumber(studyInfo.getLike()))
                .comment(String.valueOf(studyInfo.getFollowerCount()))
                .view(formatLargeNumber(studyInfo.getView()))
                .build();
    }

    /**
     * 큰 숫자를 포맷팅합니다.
     *
     * @param number 포맷팅할 숫자
     * @return String 포맷팅된 문자열
     */
    private String formatLargeNumber(Long number) {
        return number >= 1000 ? number / 1000 + "000+" : String.valueOf(number);
    }

    /**
     * 상위 문제 해결자 목록을 조회합니다.
     *
     * @return List<TopSolverDto> 상위 문제 해결자 목록
     */
    @Transactional
    public List<TopSolverDto> getTopSolvers() {
        return studyInfoRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(StudyInfo::totalSolve).reversed()
                        .thenComparing(StudyInfo::getLike).reversed())
                .limit(TOP_LIMIT)
                .map(this::mapToTopSolverDto)
                .collect(Collectors.toList());
    }

    /**
     * StudyInfo 엔티티를 TopSolverDto로 변환합니다.
     *
     * @param studyInfo StudyInfo 엔티티
     * @return TopSolverDto 변환된 DTO 객체
     */
    private TopSolverDto mapToTopSolverDto(StudyInfo studyInfo) {
        return TopSolverDto.builder()
                .name(studyInfo.getStudyName())
                .like(formatLargeNumber(studyInfo.getLike()))
                .comment(String.valueOf(studyInfo.totalSolve()))
                .view(formatLargeNumber(studyInfo.getView()))
                .build();
    }
}