package com.ssafy.alttab.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto {
    private String username;
    private String memberName;
    private String memberEmail;
    private String memberAvatarUrl;
    private String memberHtmlUrl;
    private String role;
}
