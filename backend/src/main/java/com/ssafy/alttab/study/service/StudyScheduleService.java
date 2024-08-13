package com.ssafy.alttab.study.service;

import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.common.jointable.entity.ScheduleProblem;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.problem.dto.ScheduleProblemResponseDto;
import com.ssafy.alttab.problem.entity.Problem;
import com.ssafy.alttab.problem.entity.Status;
import com.ssafy.alttab.problem.repository.ProblemRepository;
import com.ssafy.alttab.problem.repository.StatusRepository;
import com.ssafy.alttab.study.dto.DeleteScheduleProblemRequestDto;
import com.ssafy.alttab.study.dto.StudyScheduleRequestDto;
import com.ssafy.alttab.study.dto.StudyScheduleResponseDto;
import com.ssafy.alttab.study.entity.Study;
import com.ssafy.alttab.study.entity.StudySchedule;
import com.ssafy.alttab.study.repository.StudyRepository;
import com.ssafy.alttab.study.repository.StudyScheduleRepository;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    private final StatusRepository statusRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    /**
     * 스터디 스케줄 정보 불러오기
     *
     * @param studyId  스터디 ID
     * @param deadline 시작 날짜
     * @return StudyScheduleResponseDto 스터디 스케줄 정보를 담은 DTO
     */
    @Transactional(readOnly = true)
    public StudyScheduleResponseDto getStudySchedule(Long studyId, LocalDate deadline) throws StudyNotFoundException {
        StudySchedule studySchedule = studyScheduleRepository
                .findByStudyIdAndDeadline(studyId, deadline)
                .orElseThrow(() -> new EntityNotFoundException("Study schedule not found for studyId: " + studyId + " and deadline: " + deadline));
        return StudyScheduleResponseDto.builder()
                .studyId(studySchedule.getStudyId())
                .deadline(studySchedule.getDeadline())
                .studyProblems(mapToStudyProblems(studySchedule.getScheduleProblems(), studyId))
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

        Problem newProblem = problemRepository.findById((requestDto.getProblemId()))
                .orElseThrow(() -> new EntityNotFoundException("Study schedule not found for problemId: " + requestDto.getProblemId()));

        Set<Long> existingProblemIds = studySchedule.getScheduleProblems().stream()
                .map(sp -> sp.getProblem().getProblemId())
                .collect(Collectors.toSet());

        if (!existingProblemIds.contains(newProblem.getProblemId())) {
            ScheduleProblem scheduleProblem = ScheduleProblem.createStudySchedule(studySchedule, newProblem, requestDto.getPresenter());
            studySchedule.addScheduleProblem(scheduleProblem);
        }

        studyScheduleRepository.save(studySchedule);

        return requestDto;
    }

    /**
     * 스터디 스케줄에서 특정 문제들을 삭제
     *
     * @param requestDto 삭제할 목록 스케줄 조회 및 삭제할 문제번호
     */
    @Transactional
    public int deleteStudyProblems(DeleteScheduleProblemRequestDto requestDto) {
        StudySchedule studySchedule = studyScheduleRepository
                .findByStudyIdAndDeadline(requestDto.getStudyId(), requestDto.getDeadline())
                .orElseThrow(() -> new EntityNotFoundException("Study schedule not found for studyId: " + requestDto.getStudyId() + " and deadline: " + requestDto.getDeadline()));

        studySchedule.deleteScheduleProblem(requestDto.getProblemId());

        return 1;
    }

    /**
     * 스터디 스케줄 삭제
     *
     * @param studyId
     * @param deadline
     */
    @Transactional
    public int deleteStudySchedule(Long studyId, LocalDate deadline) {
        StudySchedule studySchedule = studyScheduleRepository
                .findByStudyIdAndDeadline(studyId, deadline)
                .orElseThrow(() -> new EntityNotFoundException("Study schedule not found for studyId: " + studyId + " and deadline: " + deadline));

        studyScheduleRepository.delete(studySchedule);

        return 1;
    }

    //== mapper ==//
    private List<ScheduleProblemResponseDto> mapToStudyProblems(List<ScheduleProblem> studyProblems, Long studyId) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId).orElseThrow(()-> new StudyNotFoundException(studyId));
        int size = study.getStudyProblems().size();
        return studyProblems.stream()
                .map(studyProblem -> {
                    List<Status> statuses = statusRepository.findByStudyIdAndProblemId(studyId, studyProblem.getProblem().getProblemId());
                    List<String> members = statuses.stream()
                            .map(Status::getMemberId)
                            .distinct()
                            .map(memberRepository::findById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(Member::getName)
                            .collect(Collectors.toList());
                    return ScheduleProblemResponseDto.toDto(studyProblem, members, size);
                })
                .collect(Collectors.toList());
    }
}