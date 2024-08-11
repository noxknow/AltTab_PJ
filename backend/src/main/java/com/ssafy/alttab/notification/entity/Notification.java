package com.ssafy.alttab.notification.entity;

import com.ssafy.alttab.common.entity.BaseTimeEntity;
import com.ssafy.alttab.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studyId;
    private String studyName;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static Notification createNotification(Member member, Long studyId, String studyName) {
        return Notification.builder()
                .member(member)
                .studyId(studyId)
                .studyName(studyName)
                .build();
    }
}