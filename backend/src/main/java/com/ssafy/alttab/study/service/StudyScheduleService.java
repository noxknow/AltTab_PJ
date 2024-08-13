package com.ssafy.alttab.study.service;

import com.ssafy.alttab.problem.dto.ProblemResponseDto;
import com.ssafy.alttab.study.dto.StudyScheduleRequestDto;
import com.ssafy.alttab.study.dto.StudyScheduleResponseDto;
import com.ssafy.alttab.study.entity.StudySchedule;
import com.ssafy.alttab.study.repository.StudyScheduleRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyScheduleService {

    private final StudyScheduleRepository studyScheduleRepository;

    /**
     * 스터디 스케줄 정보 불러오기
     *
     * @param studyId 스터디 ID
     * @param deadline 시작 날짜
     * @return StudyScheduleResponseDto 스터디 스케줄 정보를 담은 DTO
     */
    @Transactional(readOnly = true)
    public StudyScheduleResponseDto getStudySchedule(Long studyId, LocalDate deadline) {
        StudySchedule schedule = studyScheduleRepository.findByStudyIdAndDeadline(studyId, deadline)
                .orElseThrow(() -> new IllegalArgumentException("Invalid study ID: " + studyId));

        List<ProblemResponseDto> problemDtos = schedule.getStudyProblems().stream()
                .map(sp -> ProblemResponseDto.builder()
                        .problemId(sp.getProblem().getProblemId())
                        .title(sp.getProblem().getTitle())
                        .tag(sp.getProblem().getTag())
                        .level(sp.getProblem().getLevel())
                        .presenter(sp.getPresenter())
                        .build())
                .collect(Collectors.toList());

        return StudyScheduleResponseDto.builder()
                .studyId(schedule.getStudyId())
                .deadline(schedule.getDeadline())
                .studyProblems(problemDtos)
                .build();
    }


    /**
     * 스터디 정보 갱신하기
     *
     * @param requestDto 갱신할 스터디 스케줄 정보를 담은 DTO
     */
    @Transactional
    public StudyScheduleRequestDto updateOrCreateStudySchedule(StudyScheduleRequestDto requestDto) {
        StudySchedule schedule = studyScheduleRepository.findByStudyIdAndDeadline(requestDto.getStudyId(), requestDto.getDeadline())
                .map(existingSchedule -> {
                    existingSchedule.changeDeadline(requestDto.getDeadline());
                    existingSchedule.addStudyProblem(requestDto);
                    return existingSchedule;
                })
                .orElseGet(() -> StudySchedule.createNewSchedule(requestDto));

        studyScheduleRepository.save(schedule);
        return requestDto;
    }
}