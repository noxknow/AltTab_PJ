package com.ssafy.alttab.member.entity;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.notification.entity.Notification;
import com.ssafy.alttab.study.entity.Study;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String name;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRoleStatus role;

    @Column(name = "member_study")
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberStudy> memberStudies = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @Builder.Default
    private Long totalPoint = 0L;

    //==생성 메서드==//
    public static Member createMember(String name, String avatarUrl, MemberRoleStatus role) {
        return Member.builder()
                .name(name)
                .avatarUrl(avatarUrl)
                .role(role)
                .build();
    }

    //==비즈니스 로직==//

    /**
     * 맴버가 팔로우 한 스터디 목록 반환
     *
     * @return
     */
    public List<Study> getFollowedStudies() {
        return memberStudies.stream()
                .filter(ms -> ms.getRole() == MemberRoleStatus.FOLLOWER)
                .map(MemberStudy::getStudy)
                .collect(Collectors.toList());
    }

    public void changeAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * 멤버 스터디 관계 삭제
     *
     * @param memberStudy
     */
    public void removeMemberStudy(MemberStudy memberStudy) {
        this.memberStudies.remove(memberStudy);
    }

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

    public void incrementTotalPoint(Long value) {
        this.totalPoint += value;
    }
}
