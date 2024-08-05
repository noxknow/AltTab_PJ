package com.ssafy.alttab.community.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.community.dto.CommunityMainResponseDto;
import com.ssafy.alttab.community.dto.TopSolverDto;
import com.ssafy.alttab.community.dto.WeeklyStudyDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.entity.Problem;
import com.ssafy.alttab.study.entity.StudyInfo;
import com.ssafy.alttab.study.enums.ProblemStatus;
import com.ssafy.alttab.study.repository.StudyInfoRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CommunityServiceTest {

    @InjectMocks
    private CommunityService communityService;

    @Mock
    private StudyInfoRepository studyInfoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCommunityMain() {
        // Arrange
        when(studyInfoRepository.findByCreatedAtBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(createMockStudyInfoList());
        when(studyInfoRepository.findAll()).thenReturn(createMockStudyInfoList());

        // Act
        CommunityMainResponseDto result = communityService.getCommunityMain();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getWeeklyStudies().size());
        assertEquals(2, result.getTopSolvers().size());
    }

    @Test
    void getWeeklyStudies() {
        // Arrange
        when(studyInfoRepository.findByCreatedAtBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(createMockStudyInfoList());

        // Act
        List<WeeklyStudyDto> result = communityService.getWeeklyStudies();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Study1", result.get(0).getName());
        assertEquals("Study2", result.get(1).getName());
        assertEquals(2L, result.get(0).getFollower()); // 팔로워 수
        assertEquals(1L, result.get(1).getFollower()); // 팔로워 수
    }

    @Test
    void getTopSolvers() {
        // Arrange
        when(studyInfoRepository.findAll()).thenReturn(createMockStudyInfoList());

        // Act
        List<TopSolverDto> result = communityService.getTopSolvers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Study1", result.get(0).getName());
        assertEquals("Study2", result.get(1).getName());
        assertEquals(2L, result.get(0).getTotalSolve());
        assertEquals(1L, result.get(1).getTotalSolve());
    }

    private List<StudyInfo> createMockStudyInfoList() {
        StudyInfo study1 = StudyInfo.builder()
                .studyName("Study1")
                .like(10L)
                .view(100L)
                .memberStudies(createMockMemberStudies(3, 2))
                .problems(createMockProblems(3, 2))
                .build();

        StudyInfo study2 = StudyInfo.builder()
                .studyName("Study2")
                .like(8L)
                .view(80L)
                .memberStudies(createMockMemberStudies(2, 1))
                .problems(createMockProblems(2, 1))
                .build();

        return Arrays.asList(study1, study2);
    }

    private List<MemberStudy> createMockMemberStudies(int totalMembers, int followers) {
        List<MemberStudy> memberStudies = new ArrayList<>();
        for (int i = 0; i < totalMembers; i++) {
            Member member = Member.builder().memberId((long) i).build();
            MemberRoleStatus role = i < followers ? MemberRoleStatus.FOLLOWER : MemberRoleStatus.TEAM_MEMBER;
            MemberStudy memberStudy = MemberStudy.builder()
                    .member(member)
                    .role(role)
                    .build();
            memberStudies.add(memberStudy);
        }
        return memberStudies;
    }

    private List<Problem> createMockProblems(int totalProblems, int solvedProblems) {
        List<Problem> problems = new ArrayList<>();
        for (int i = 0; i < totalProblems; i++) {
            ProblemStatus status = i < solvedProblems ? ProblemStatus.DONE : ProblemStatus.NOT_START;
            Problem problem = Problem.builder()
                    .id((long) i)
                    .problemStatus(status)
                    .build();
            problems.add(problem);
        }
        return problems;
    }
}