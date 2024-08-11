package com.ssafy.alttab.study.entity;

import com.ssafy.alttab.common.entity.BaseTimeEntity;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.enums.ProblemStatus;
import java.util.ArrayList;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(name = "view_count")
    private Long view;

    @Column(name = "like_count")
    private Long like;

    @Column(name = "member_study")
    @Builder.Default
    @OneToMany(mappedBy = "study", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<MemberStudy> memberStudies = new ArrayList<>();

//    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<Problem> problems = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<StudyProblem> studyProblems = new ArrayList<>();

    //==비즈니스 로직==//
    /**
     * 스터디에서 완료된 문제의 총 개수를 반환합니다.
     *
     * @return 완료된 문제의 총 개수
     */
//    public Long totalSolve() {
//        return problems.stream()
//                .filter(problem -> problem.getProblemStatus() == ProblemStatus.DONE)
//                .count();
//    }

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

    /**
     * 스터디를 팔로우하는 모든 멤버의 목록을 반환합니다.
     *
     * @return 팔로워 멤버 목록
     */
    public List<Member> getFollowers() {
        return memberStudies.stream()
                .filter(ms -> ms.getRole() == MemberRoleStatus.FOLLOWER)
                .map(MemberStudy::getMember)
                .collect(Collectors.toList());
    }

    public void updateStudy(StudyInfoRequestDto dto) {
        this.studyName = dto.getStudyName();
        this.studyDescription = dto.getStudyDescription();
    }

    public void addStudyProblem(StudyProblem studyProblem) {
        this.studyProblems.add(studyProblem);
    }

}