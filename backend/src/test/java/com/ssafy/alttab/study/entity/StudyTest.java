//package com.ssafy.alttab.study.entity;
//
//import com.ssafy.alttab.common.jointable.entity.MemberStudy;
//import com.ssafy.alttab.member.entity.Member;
//import com.ssafy.alttab.member.enums.MemberRoleStatus;
//import com.ssafy.alttab.study.enums.ProblemStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class StudyTest {
//
//    @Mock
//    private Problem problem1, problem2, problem3;
//
//    @Mock
//    private MemberStudy memberStudy1, memberStudy2, memberStudy3;
//
//    @Mock
//    private Member member1, member2, member3;
//
//    private Study study;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        study = Study.builder()
//                .problems(Arrays.asList(problem1, problem2, problem3))
//                .memberStudies(Arrays.asList(memberStudy1, memberStudy2, memberStudy3))
//                .build();
//    }
//
//    @Test
//    void testTotalSolve() {
//        when(problem1.getProblemStatus()).thenReturn(ProblemStatus.DONE);
//        when(problem2.getProblemStatus()).thenReturn(ProblemStatus.IN_PROGRESS);
//        when(problem3.getProblemStatus()).thenReturn(ProblemStatus.DONE);
//
//        assertEquals(2, study.totalSolve());
//    }
//
//    @Test
//    void testGetFollowerCount() {
//        when(memberStudy1.getRole()).thenReturn(MemberRoleStatus.FOLLOWER);
//        when(memberStudy2.getRole()).thenReturn(MemberRoleStatus.MEMBER);
//        when(memberStudy3.getRole()).thenReturn(MemberRoleStatus.FOLLOWER);
//
//        assertEquals(2, study.getFollowerCount());
//    }
//
//    @Test
//    void testGetFollowers() {
//        when(memberStudy1.getRole()).thenReturn(MemberRoleStatus.FOLLOWER);
//        when(memberStudy2.getRole()).thenReturn(MemberRoleStatus.MEMBER);
//        when(memberStudy3.getRole()).thenReturn(MemberRoleStatus.FOLLOWER);
//
//        when(memberStudy1.getMember()).thenReturn(member1);
//        when(memberStudy3.getMember()).thenReturn(member3);
//
//        List<Member> followers = study.getFollowers();
//        assertEquals(2, followers.size());
//        assertTrue(followers.contains(member1));
//        assertTrue(followers.contains(member3));
//        assertFalse(followers.contains(member2));
//    }
//}