package com.ssafy.alttab.study.entity;

import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.problem.dto.ProblemRequestDto;
import com.ssafy.alttab.problem.entity.Problem;
import com.ssafy.alttab.study.dto.StudyScheduleRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StudySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_schedule_id")
    private Long id;

    @Column(name = "start_name")
    private LocalDateTime startDay;

    @OneToMany(mappedBy = "studySchedule", fetch = FetchType.LAZY)
    private List<StudyProblem> studyProblems = new ArrayList<>();

    //== 생성 메서드 ==//
    public static StudySchedule createNewSchedule(StudyScheduleRequestDto requestDto) {
        return StudySchedule.builder()
                .startDay(requestDto.getStartDate())
                .studyProblems(new ArrayList<>())
                .build();
    }

    /**
     * problem 문제 추가
     * @param requestDto 요청 데이터
     */
    public void addStudyProblem(StudyScheduleRequestDto requestDto) {
        List<Long> existingProblemIds = this.studyProblems.stream()
                .map(sp -> sp.getProblem().getProblemId())
                .toList();

        for (ProblemRequestDto problemDto : requestDto.getProblemRequestDtos()) {
            if (!existingProblemIds.contains(problemDto.getProblemId())) {
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
                        .deadline(problemDto.getDeadline())
                        .build();

                this.studyProblems.add(studyProblem);
            }
        }
    }
}
