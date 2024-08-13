package com.ssafy.alttab.study.entity;

import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.problem.dto.ProblemRequestDto;
import com.ssafy.alttab.problem.entity.Problem;
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

    @Builder.Default
    @OneToMany(mappedBy = "studySchedule", cascade = CascadeType.ALL)
    private List<StudyProblem> studyProblems = new ArrayList<>();

    //== 생성 메서드 ==//
    public static StudySchedule createNewSchedule(StudyScheduleRequestDto requestDto) {
        StudySchedule schedule = StudySchedule.builder()
                .studyId(requestDto.getStudyId())
                .deadline(requestDto.getDeadline())
                .build();

        schedule.addStudyProblem(requestDto);
        return schedule;
    }

    /**
     * problem 문제 추가
     * @param requestDto 요청 데이터
     */
    public void addStudyProblem(StudyScheduleRequestDto requestDto) {
        for (ProblemRequestDto problemDto : requestDto.getStudyProblems()) {
            Problem problem = Problem.builder()
                    .problemId(problemDto.getProblemId())
                    .title(problemDto.getTitle())
                    .tag(problemDto.getTag())
                    .level(problemDto.getLevel())
                    .build();

            StudyProblem studyProblem = StudyProblem.builder()
                    .studySchedule(this)
                    .problem(problem)
                    .presenter(problemDto.getPresenter())
                    .build();

            this.studyProblems.add(studyProblem);
        }
    }

    public void changeDeadline(LocalDate deadline){
        this.deadline = deadline;
    }
}
