package com.ssafy.alttab.member.entity;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.security.oauth2.dto.OAuth2Response;
import com.ssafy.alttab.study.entity.StudyInfo;
import jakarta.persistence.*;
import com.ssafy.alttab.member.dto.MemberRequestDto;
import com.ssafy.alttab.member.dto.MemberResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column
    private String memberAvatarUrl;

    @Column
    private String memberHtmlUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRoleStatus role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<MemberStudy> memberStudies = new ArrayList<>();

    //==생성 메서드==//

    //==연관관계 메서드==//

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

    public void fromDto(MemberRequestDto memberDto) {
        this.memberName = memberDto.getMemberName();
        this.memberEmail = memberDto.getMemberEmail();
        this.memberAvatarUrl = memberDto.getMemberAvatarUrl();
        this.memberHtmlUrl = memberDto.getMemberHtmlUrl();
    }

    public void fromOAuth2(OAuth2Response oAuth2Response) {
        this.memberName = oAuth2Response.getName();
        this.memberEmail = oAuth2Response.getEmail();
        this.memberAvatarUrl = oAuth2Response.getAvatarUrl();
        this.memberHtmlUrl = oAuth2Response.getHtmlUrl();
    }

    public MemberResponseDto toDto() {
        return MemberResponseDto.builder()
                .username(this.username)
                .memberName(this.memberName)
                .memberEmail(this.memberEmail)
                .memberAvatarUrl(this.memberAvatarUrl)
                .memberHtmlUrl(this.memberHtmlUrl)
                .role(String.valueOf(this.role))
                .build();
    }
}
