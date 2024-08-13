package com.ssafy.alttab.problem.dto;

import com.ssafy.alttab.common.jointable.entity.ScheduleProblem;
import com.ssafy.alttab.problem.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleProblemResponseDto {
    private Long studyScheduleId;
    private Long problemId;
    private String title;
    private String tag;
    private Long level;
    private String presenter;

    public static ScheduleProblemResponseDto toDto(ScheduleProblem scheduleProblem) {
        Problem problem = scheduleProblem.getProblem();
        return ScheduleProblemResponseDto.builder()
                .studyScheduleId(scheduleProblem.getId())
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .tag(problem.getTag())
                .level(problem.getLevel())
                .presenter(scheduleProblem.getPresenter())
                .build();
    }
}
