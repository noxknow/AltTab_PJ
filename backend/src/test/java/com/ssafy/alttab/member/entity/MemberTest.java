package com.ssafy.alttab.member.entity;

import org.junit.jupiter.api.BeforeEach;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.entity.StudyInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberTest {

    @Mock
    private MemberStudy memberStudy1, memberStudy2, memberStudy3;

    @Mock
    private StudyInfo studyInfo1, studyInfo2, studyInfo3;

    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = Member.builder()
                .memberStudies(Arrays.asList(memberStudy1, memberStudy2, memberStudy3))
                .build();
    }

    @Test
    void testGetFollowedStudies() {
        when(memberStudy1.getRole()).thenReturn(MemberRoleStatus.FOLLOWER);
        when(memberStudy2.getRole()).thenReturn(MemberRoleStatus.MEMBER);
        when(memberStudy3.getRole()).thenReturn(MemberRoleStatus.FOLLOWER);

        when(memberStudy1.getStudyInfo()).thenReturn(studyInfo1);
        when(memberStudy3.getStudyInfo()).thenReturn(studyInfo3);

        List<StudyInfo> followedStudies = member.getFollowedStudies();
        assertEquals(2, followedStudies.size());
        assertTrue(followedStudies.contains(studyInfo1));
        assertTrue(followedStudies.contains(studyInfo3));
        assertFalse(followedStudies.contains(studyInfo2));
    }
}