package com.ssafy.alttab.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MemberListResponseDto {
    List<MemberSearchResponseDto> members;
}
