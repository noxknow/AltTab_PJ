package com.ssafy.alttab.problem.dto;

import com.ssafy.alttab.common.jointable.entity.ScheduleProblem;
import com.ssafy.alttab.problem.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
    private List<String> members;
    private int size;
    private boolean check;

    public static ScheduleProblemResponseDto toDto(ScheduleProblem scheduleProblem, List<String> members, int size, String username) {
        Problem problem = scheduleProblem.getProblem();
        return ScheduleProblemResponseDto.builder()
                .studyScheduleId(scheduleProblem.getId())
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .tag(problem.getTag())
                .level(problem.getLevel())
                .presenter(scheduleProblem.getPresenter())
                .members(members)
                .size(size)
                .check(!members.contains(username))
                .build();
    }
}
