package com.ssafy.alttab.member.entity;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.entity.Problem;
import com.ssafy.alttab.study.entity.StudyInfo;
import com.ssafy.alttab.study.enums.ProblemStatus;
import jakarta.persistence.*;
import java.util.stream.Collectors;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false)
    private String memberName;

    @Column
    private String memberEmail;

    @Column(nullable = false)
    private String memberAvatarUrl;

    @Column(nullable = false)
    private String memberHtmlUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRoleStatus role;

    @OneToMany(mappedBy = "member")
    private List<MemberStudy> memberStudies;

    //==비즈니스 로직==//

    /**
     * 맴버가 팔로우 한 스터디 목록 반환
     *
     * @return
     */
    public List<StudyInfo> getFollowedStudies() {
        return memberStudies.stream()
                .filter(ms -> ms.getRole() == MemberRoleStatus.FOLLOWER)
                .map(MemberStudy::getStudyInfo)
                .collect(Collectors.toList());
    }
}
