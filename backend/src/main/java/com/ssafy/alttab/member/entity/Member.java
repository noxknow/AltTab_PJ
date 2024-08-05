package com.ssafy.alttab.member.entity;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.entity.StudyInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private List<MemberStudy> memberStudies = new ArrayList<>();

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
