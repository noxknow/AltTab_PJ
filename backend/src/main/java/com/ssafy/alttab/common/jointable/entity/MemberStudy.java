package com.ssafy.alttab.common.jointable.entity;

import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.study.entity.StudyInfo;
import jakarta.persistence.*;
import lombok.Getter;

@Table
@Entity
@Getter
public class MemberStudy {
    @Id
    private long memberStudyId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private StudyInfo studyInfo;

    private String role;
}
