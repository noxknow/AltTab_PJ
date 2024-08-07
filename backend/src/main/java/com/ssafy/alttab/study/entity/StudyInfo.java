package com.ssafy.alttab.study.entity;

import com.ssafy.alttab.common.entity.BaseTimeEntity;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.dto.MemberRequestDto;
import com.ssafy.alttab.member.dto.MemberResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.dto.StudyInfoResponseDto;
import com.ssafy.alttab.study.enums.ProblemStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
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
public class StudyInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_info_id")
    private Long id;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String studyName;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String studyDescription;

    @Column(name = "view_count")
    private Long view;

    @Column(name = "like_count")
    private Long like;

    @ElementCollection
    @CollectionTable(name = "study_emails", joinColumns = @JoinColumn(name = "study_id"))
    @Column(name = "email")
    @Builder.Default
    private List<String> studyEmails = new ArrayList<>();

    @Column(name = "member_study")
    @OneToMany(mappedBy = "studyInfo", cascade = CascadeType.ALL)
    private List<MemberStudy> memberStudies = new ArrayList<>();

    @OneToMany(mappedBy = "studyInfo", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Problem> problems = new ArrayList<>();

    //==생성 메서드==//
    //==비즈니스 로직==//
    public static StudyInfo createStudy(StudyInfoRequestDto studyInfoRequestDto) {
        return StudyInfo.builder()
                .studyName(studyInfoRequestDto.getStudyName())
                .studyDescription(studyInfoRequestDto.getStudyDescription())
                .view(0L)
                .like(0L)
                .studyEmails(studyInfoRequestDto.getStudyEmails())
                .build();
    }

    public StudyInfoResponseDto toDto() {
        List<Member> studyMembers = this.memberStudies.stream()
                .filter(memberStudy -> !memberStudy.getRole().equals(MemberRoleStatus.FOLLOWER))
                .map(MemberStudy::getMember)
                .toList();

        return StudyInfoResponseDto.builder()
                .studyId(this.id)
                .studyName(this.studyName)
                .studyDescription(this.studyDescription)
                .view(this.view)
                .like(this.like)
                .problems(this.problems)
                .studyMembers(studyMembers)
                .build();
    }
    //==비즈니스 로직==//

    /**
     * 스터디에서 완료된 문제의 총 개수를 반환합니다.
     *
     * @return 완료된 문제의 총 개수
     */
    public Long totalSolve() {
        return problems.stream()
                .filter(problem -> problem.getProblemStatus() == ProblemStatus.DONE)
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

    public void fromDto(StudyInfoRequestDto studyInfoRequestDto) {
        this.studyName = studyInfoRequestDto.getStudyName();
        this.studyDescription = studyInfoRequestDto.getStudyDescription();
    }

}