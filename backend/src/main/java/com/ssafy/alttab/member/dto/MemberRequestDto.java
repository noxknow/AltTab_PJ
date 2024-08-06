package com.ssafy.alttab.member.dto;

import lombok.*;

@Getter
@Setter
public class MemberRequestDto {
    private String username;
    private String memberName;
    private String memberEmail;
    private String memberAvatarUrl;
    private String memberHtmlUrl;
    private String role;
}
