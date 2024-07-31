package com.ssafy.alttab.member.entity;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
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
    private String username;
    private String memberName;
    private String memberEmail;
    private String memberAvatarUrl;
    private String memberHtmlUrl;
    private String role;

    @OneToMany(mappedBy = "member")
    private List<MemberStudy> memberStudies = new ArrayList<>();


}
