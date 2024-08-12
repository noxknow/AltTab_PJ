package com.ssafy.alttab.study.service;

import com.ssafy.alttab.common.exception.ScheduleNotFoundException;
import com.ssafy.alttab.problem.dto.ProblemResponseDto;
import com.ssafy.alttab.study.dto.StudyScheduleRequestDto;
import com.ssafy.alttab.study.dto.StudyScheduleResponseDto;
import com.ssafy.alttab.study.entity.StudySchedule;
import com.ssafy.alttab.study.repository.StudyScheduleRepository;
import java.time.LocalDateTime;
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
     * @param studyScheduleId 스터디 스케줄 ID
     * @param startDate 시작 날짜
     * @return StudyScheduleResponseDto 스터디 스케줄 정보를 담은 DTO
     */
    @Transactional(readOnly = true)
    public StudyScheduleResponseDto getStudySchedule(Long studyScheduleId, LocalDateTime startDate) {
        StudySchedule studySchedule = studyScheduleRepository.findByIdAndStartDay(studyScheduleId, startDate)
                .orElseThrow(() -> new ScheduleNotFoundException(studyScheduleId));

        List<ProblemResponseDto> problemResponseDtos = studySchedule.getStudyProblems().stream()
                .map(ProblemResponseDto::toDto)
                .collect(Collectors.toList());

        return StudyScheduleResponseDto.builder()
                .studyId(studySchedule.getId())
                .startDate(studySchedule.getStartDay())
                .problemResponseDtos(problemResponseDtos)
                .build();
    }

    /**
     * 스터디 정보 갱신하기
     *
     * @param requestDto 갱신할 스터디 스케줄 정보를 담은 DTO
     */
    @Transactional
    public StudyScheduleRequestDto updateOrCreateStudySchedule(StudyScheduleRequestDto requestDto) {
        StudySchedule studySchedule = studyScheduleRepository
                .findByIdAndStartDay(requestDto.getStudyId(), requestDto.getStartDate())
                .orElseGet(() -> StudySchedule.createNewSchedule(requestDto));

        studySchedule.addStudyProblem(requestDto);
        studyScheduleRepository.save(studySchedule);

        return requestDto;
    }
}