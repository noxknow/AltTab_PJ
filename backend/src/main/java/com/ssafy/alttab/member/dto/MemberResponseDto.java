package com.ssafy.alttab.member.dto;

import com.ssafy.alttab.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {
    private String name;
    private String avatarUrl;

    public static MemberResponseDto toDto(Member member) {
        return MemberResponseDto.builder()
                .name(member.getName())
                .avatarUrl(member.getAvatarUrl())
                .build();
    }
}
