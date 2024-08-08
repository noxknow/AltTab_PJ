package com.ssafy.alttab.member.dto;

import com.ssafy.alttab.study.dto.JoinedStudyResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberJoinedStudiesResponseDto {

    List<JoinedStudyResponseDto> joinedStudies;
}
