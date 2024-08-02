package com.ssafy.alttab.member.entity;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import jakarta.persistence.*;
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
    private String role;

    @OneToMany(mappedBy = "member")
    private List<MemberStudy> memberStudies;

}
