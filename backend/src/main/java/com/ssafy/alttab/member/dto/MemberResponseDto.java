package com.ssafy.alttab.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto {
    private String username;
    private String memberName;
    private String memberEmail;
    private String memberAvatarUrl;
    private String memberHtmlUrl;
    private String role;
}
