package com.ssafy.alttab.study.service;

import com.ssafy.alttab.common.jointable.entity.ScheduleProblem;
import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.member.dto.MemberInfoResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.problem.dto.ProblemResponseDto;
import com.ssafy.alttab.problem.entity.Problem;
import com.ssafy.alttab.problem.repository.ProblemRepository;
import com.ssafy.alttab.study.dto.StudyScheduleRequestDto;
import com.ssafy.alttab.study.dto.StudyScheduleResponseDto;
import com.ssafy.alttab.study.entity.StudySchedule;
import com.ssafy.alttab.study.repository.StudyScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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
    private final ProblemRepository problemRepository;

    /**
     * 스터디 스케줄 정보 불러오기
     *
     * @param studyId 스터디 ID
     * @param deadline 시작 날짜
     * @return StudyScheduleResponseDto 스터디 스케줄 정보를 담은 DTO
     */
    @Transactional(readOnly = true)
    public StudyScheduleResponseDto getStudySchedule(Long studyId, LocalDate deadline) {
        StudySchedule studySchedule = studyScheduleRepository
                .findByStudyIdAndDeadline(studyId, deadline)
                .orElseThrow(() -> new EntityNotFoundException("Study schedule not found for studyId: " + studyId + " and deadline: " + deadline));

        return StudyScheduleResponseDto.builder()
                .studyId(studySchedule.getStudyId())
                .deadline(studySchedule.getDeadline())
                .studyProblems(mapToStudyProblems(studySchedule.getScheduleProblems()))
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
                .findByStudyIdAndDeadline(requestDto.getStudyId(), requestDto.getDeadline())
                .orElseGet(() -> StudySchedule.createNewStudySchedule(requestDto));

        List<Problem> newProblems = problemRepository.findAllById(requestDto.getProblemId());

        Set<Long> existingProblemIds = studySchedule.getScheduleProblems().stream()
                .map(sp -> sp.getProblem().getProblemId())
                .collect(Collectors.toSet());

        for (Problem problem : newProblems) {
            if (!existingProblemIds.contains(problem.getProblemId())) {
                ScheduleProblem scheduleProblem = ScheduleProblem.createStudySchedule(studySchedule, problem, requestDto.getPresenter());
                studySchedule.addScheduleProblem(scheduleProblem);
            }
        }

        studyScheduleRepository.save(studySchedule);

        return requestDto;
    }

    //== mapper ==//

    private List<ProblemResponseDto> mapToStudyProblems(List<ScheduleProblem> studyProblems) {
        return studyProblems.stream()
                .map(ProblemResponseDto::toDto)
                .collect(Collectors.toList());
    }

}