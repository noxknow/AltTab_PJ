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

    public static MemberInfoResponseDto toDto(Member member) {
        return MemberInfoResponseDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .avatarUrl(member.getAvatarUrl())
                .build();
    }
}
