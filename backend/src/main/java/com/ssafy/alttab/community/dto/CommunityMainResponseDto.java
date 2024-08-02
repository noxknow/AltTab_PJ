package com.ssafy.alttab.community.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityMainResponseDto {
    private List<WeeklyStudyDto> weeklyStudies;
    private List<TopSolverDto> topSolvers;
}
