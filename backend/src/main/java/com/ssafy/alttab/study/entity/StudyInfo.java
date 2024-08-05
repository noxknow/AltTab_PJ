package com.ssafy.alttab.study.entity;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.enums.ProblemStatus;
import jakarta.persistence.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.parsing.Problem;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_info_id")
    private Long id;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String studyName;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String studyDescription;

    @Column(name = "member_study")
    @OneToMany(mappedBy = "studyInfo", cascade = CascadeType.ALL)
    private List<MemberStudy> memberStudies;

    @ElementCollection
    @CollectionTable(name = "study_emails", joinColumns = @JoinColumn(name = "study_id"))
    @Column(name = "email")
    private List<String> studyEmails;

    @OneToMany(mappedBy = "studyInfo", cascade = CascadeType.ALL)
    private List<Problem> problems;

    //==비즈니스 로직==//
    public static StudyInfo createStudy(StudyInfoRequestDto studyInfoRequestDto) {

        return StudyInfo.builder()
                .studyName(studyInfoRequestDto.getStudyName())
                .studyDescription(studyInfoRequestDto.getStudyDescription())
                .studyEmails(studyInfoRequestDto.getStudyEmails())
                .build();
    }

    /**
     * 스터디에서 완료된 문제의 총 개수를 반환합니다.
     *
     * @return 완료된 문제의 총 개수
     */
    public int totalSolve(){
        return (int) problems.stream()
                .filter(problem -> problem.getProblemStatus() == ProblemStatus.DONE)
                .count();
    }

    /**
     * 스터디의 총 팔로워 수를 반환합니다.
     *
     * @return 스터디의 팔로워 수
     */
    public int getFollowerCount() {
        return (int) memberStudies.stream()
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
}