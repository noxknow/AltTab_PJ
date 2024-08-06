package com.ssafy.alttab.common.jointable.entity;

import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.entity.StudyInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "member_study")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberStudy {
    @Id
    private long memberStudyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "study_info_id")
    private StudyInfo studyInfo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRoleStatus role;

    //    public void changeRole(MemberRoleStatus newRole) {
//        this.role = newRole;
//    }
    //==생성 메서드==//
    public static MemberStudy createMemberStudy(Member member, StudyInfo studyInfo, MemberRoleStatus role) {
        return MemberStudy.builder()
                .member(member)
                .studyInfo(studyInfo)
                .role(role)
                .build();
    }
}
