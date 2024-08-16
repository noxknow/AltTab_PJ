package com.ssafy.alttab.problem.dto;

import com.ssafy.alttab.member.dto.MemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SolveMembersResponseDto {
    private List<MemberResponseDto> members;
}
