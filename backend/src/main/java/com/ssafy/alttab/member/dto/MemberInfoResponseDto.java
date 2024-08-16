package com.ssafy.alttab.member.dto;

import com.ssafy.alttab.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponseDto {

    private Long memberId;
    private String name;
    private String avatarUrl;
    private Long memberPoint;

    public static MemberInfoResponseDto toDto(Member member, Long memberPoint) {
        return MemberInfoResponseDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .avatarUrl(member.getAvatarUrl())
                .memberPoint(memberPoint)
                .build();
    }
}
