package com.ssafy.alttab.member.dto;

import com.ssafy.alttab.study.dto.JoinedStudyResponseDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberJoinedStudiesResponseDto {
    List<JoinedStudyResponseDto> joinedStudies;
}
