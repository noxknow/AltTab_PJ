package com.ssafy.alttab.study.entity;

import com.ssafy.alttab.common.jointable.entity.ScheduleProblem;
import com.ssafy.alttab.study.dto.StudyScheduleRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class StudySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_schedule_id")
    private Long id;

    @Column(name = "study_id")
    private Long studyId;

    @Column(name = "deadline")
    private LocalDate deadline;

    @OneToMany(mappedBy = "studySchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ScheduleProblem> ScheduleProblems = new ArrayList<>();

    //== 생성 메서드 ==//
    public static StudySchedule createNewStudySchedule(StudyScheduleRequestDto requestDto){
        return StudySchedule.builder()
                .studyId(requestDto.getStudyId())
                .deadline(requestDto.getDeadline())
                .ScheduleProblems(new ArrayList<>())
                .build();
    }

    //== 비즈니스 로직 ==//

    /**
     * 문제 추가
     *
     * @param scheduleProblem 추가할 문제
     */
    public void addScheduleProblem(ScheduleProblem scheduleProblem) {
        this.getScheduleProblems().add(scheduleProblem);
        scheduleProblem.changeStudySchedule(this);
    }

    /**
     * 문제 삭제
     * @param problemId 문제 번호
     */
    public void deleteScheduleProblem(Long problemId){
        this.getScheduleProblems().removeIf(scheduleProblem ->
                scheduleProblem.getProblem().getProblemId().equals(problemId));
    }
}
