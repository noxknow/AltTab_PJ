package com.ssafy.alttab.member.dto;

import com.ssafy.alttab.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchResponseDto {
    private Long memberId;
    private String memberName;
    private String avatarUrl;

    public static MemberSearchResponseDto toDto(Member member) {
        return MemberSearchResponseDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .avatarUrl(member.getAvatarUrl())
                .build();
    }
}
