package com.ssafy.alttab.member.dto;

import com.ssafy.alttab.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberSearchResponseDto {
    private Long memberId;
    private String name;
    private String avatarUrl;

    public static MemberSearchResponseDto toDto(Member member) {
        return MemberSearchResponseDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .avatarUrl(member.getAvatarUrl())
                .build();
    }
}
