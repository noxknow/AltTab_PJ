package com.ssafy.alttab.study.entity;

import com.ssafy.alttab.common.entity.BaseTimeEntity;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;

import java.util.ArrayList;

import com.ssafy.alttab.study.enums.ProblemStatus;
import jakarta.persistence.*;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Study extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String studyName;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String studyDescription;

    @Column
    @Builder.Default
    private Long studyPoint = 0L;

    @Column
    @Builder.Default
    private Long solveCount = 0L;

    @Column(name = "member_study")
    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberStudy> memberStudies = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StudyProblem> studyProblems = new ArrayList<>();

    //==생성 메서드==//
    public static Study createStudy(String studyName, String studyDescription) {
        return Study.builder()
                .studyName(studyName)
                .studyDescription(studyDescription)
                .build();
    }

    //==비즈니스 로직==//

    /**
     * 스터디에서 완료된 문제의 총 개수를 반환합니다.
     *
     * @return 완료된 문제의 총 개수
     */
    public Long totalSolve() {
        return studyProblems.stream()
                .filter(studyProblem -> studyProblem.getProblemStatus() == ProblemStatus.DONE)
                .count();
    }

    /**
     * 스터디의 총 팔로워 수를 반환합니다.
     *
     * @return 스터디의 팔로워 수
     */
    public Long getFollowerCount() {
        return memberStudies.stream()
                .filter(ms -> ms.getRole() == MemberRoleStatus.FOLLOWER)
                .count();
    }

    public void updateStudy(StudyInfoRequestDto dto) {
        this.studyName = dto.getStudyName();
        this.studyDescription = dto.getStudyDescription();
    }

    public void addStudyProblem(StudyProblem studyProblem) {
        this.studyProblems.add(studyProblem);
    }

    public void addMemberStudy(MemberStudy memberStudy) {
        this.memberStudies.add(memberStudy);
        Member member = memberStudy.getMember();
        member.getMemberStudies().add(memberStudy);
    }

    public void removeMemberStudy(MemberStudy memberStudy) {
        this.memberStudies.remove(memberStudy);
    }

    public void incrementStudyPoint(Long value) {
        this.studyPoint += value;
    }

    public void incrementSolveCount() {
        this.solveCount++;
    }
}