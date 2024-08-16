package com.ssafy.alttab.common.jointable.entity;

import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.entity.Study;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberStudyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRoleStatus role;

    @Builder.Default
    private Long memberPoint = 0L;

    //==생성 메서드==//
    public static MemberStudy createMemberStudy(Member member, Study study, MemberRoleStatus role) {
        return MemberStudy.builder()
                .member(member)
                .study(study)
                .role(role)
                .build();
    }

    public void incrementMemberPoint(Long value) {
        this.memberPoint += value;
    }

    //==연관 관계 메서드==//

}
