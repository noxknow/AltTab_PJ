package com.ssafy.alttab.member.entity;

import com.ssafy.alttab.joinTable.entity.MemberStudy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String memberKey;
    private String memberName;
    private String email;
    private String avatarUrl;
    private String htmlUrl;
    private String role;

    @OneToMany(mappedBy = "member")
    private List<MemberStudy> memberStudies = new ArrayList<>();
}
