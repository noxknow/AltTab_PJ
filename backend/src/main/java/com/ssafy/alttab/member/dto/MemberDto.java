package com.ssafy.alttab.member.dto;

import com.ssafy.alttab.member.entity.Member;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String username;
    private String memberName;
    private String memberEmail;
    private String memberAvatarUrl;
    private String memberHtmlUrl;
    private String role;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .username(member.getUsername())
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .memberAvatarUrl(member.getMemberAvatarUrl())
                .memberHtmlUrl(member.getMemberHtmlUrl())
                .role(member.getRole())
                .build();
    }
}
