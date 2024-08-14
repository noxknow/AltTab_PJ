package com.ssafy.alttab.study.service;

import com.ssafy.alttab.common.jointable.entity.ScheduleProblem;
import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.common.jointable.repository.MemberStudyRepository;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.problem.dto.ScheduleProblemResponseDto;
import com.ssafy.alttab.problem.entity.Problem;
import com.ssafy.alttab.problem.entity.Status;
import com.ssafy.alttab.problem.repository.ProblemRepository;
import com.ssafy.alttab.problem.repository.StatusRepository;
import com.ssafy.alttab.study.dto.DeadlinesResponseDto;
import com.ssafy.alttab.study.dto.DeleteScheduleProblemRequestDto;
import com.ssafy.alttab.study.dto.StudyScheduleRequestDto;
import com.ssafy.alttab.study.dto.StudyScheduleResponseDto;
import com.ssafy.alttab.study.entity.Study;
import com.ssafy.alttab.study.entity.StudySchedule;
import com.ssafy.alttab.study.repository.StudyRepository;
import com.ssafy.alttab.study.repository.StudyScheduleRepository;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.alttab.common.jointable.entity.StudyProblem.createStudyProblem;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyScheduleService {

    private final StudyScheduleRepository studyScheduleRepository;
    private final ProblemRepository problemRepository;
    private final StatusRepository statusRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final MemberStudyRepository memberStudyRepository;

    /**
     * 스터디 스케줄 정보 불러오기
     *
     * @param studyId  스터디 ID
     * @param deadline 시작 날짜
     * @return StudyScheduleResponseDto 스터디 스케줄 정보를 담은 DTO
     */
    @Transactional(readOnly = true)
    public StudyScheduleResponseDto getStudySchedule(String username, Long studyId, LocalDate deadline) {
        StudySchedule studySchedule = studyScheduleRepository
                .findByStudyIdAndDeadline(studyId, deadline)
                .orElseThrow(() -> new EntityNotFoundException("Study schedule not found for studyId: " + studyId + " and deadline: " + deadline));
        return StudyScheduleResponseDto.builder()
                .studyId(studySchedule.getStudyId())
                .deadline(studySchedule.getDeadline())
                .studyProblems(mapToStudyProblems(username, studySchedule.getScheduleProblems(), studyId))
                .build();
    }

    /**
     * 스터디 정보 갱신하기
     *
     * @param requestDto 갱신할 스터디 스케줄 정보를 담은 DTO
     */
    @Transactional
    public StudyScheduleRequestDto updateOrCreateStudySchedule(String name, StudyScheduleRequestDto requestDto) {
        // StudyProblem
        int memberCount = memberStudyRepository.findByStudyId(requestDto.getStudyId()).size();
        Long problemId = requestDto.getProblemId();
        Long studyId = requestDto.getStudyId();
        LocalDate deadline = requestDto.getDeadline();
        Study study = studyRepository.findById(requestDto.getStudyId())
                .orElseThrow(() -> new EntityNotFoundException("Study not found for studyId: " + studyId));
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException("Problem not found for problemId: " + problemId));
        boolean isDuplicate = study.getStudyProblems().stream()
                .anyMatch(sp -> sp.getProblem().getProblemId().equals(problemId) &&
                        sp.getDeadline().equals(deadline));
        if (!isDuplicate) {
            StudyProblem newStudyProblem = createStudyProblem(study, problem, deadline, name, memberCount);
            study.addStudyProblem(newStudyProblem);
        }

        // StudySchedule
        StudySchedule studySchedule = studyScheduleRepository
                .findByStudyIdAndDeadline(requestDto.getStudyId(), requestDto.getDeadline())
                .orElseGet(() -> StudySchedule.createNewStudySchedule(requestDto));

        Problem newProblem = problemRepository.findById((requestDto.getProblemId()))
                .orElseThrow(() -> new EntityNotFoundException("Problem not found for problemId: " + requestDto.getProblemId()));

        boolean problemAlreadyExists = studySchedule.getScheduleProblems().stream()
                .anyMatch(sp -> sp.getProblem().getProblemId().equals(newProblem.getProblemId())
                        && sp.getStudySchedule().getDeadline().equals(requestDto.getDeadline()));

        if (!problemAlreadyExists) {
            ScheduleProblem scheduleProblem = ScheduleProblem.createStudySchedule(studySchedule, newProblem, name);
            studySchedule.addScheduleProblem(scheduleProblem);
        }

        studyScheduleRepository.save(studySchedule);
        studyRepository.save(study);
        return requestDto;
    }

    /**
     * 스터디 스케줄에서 특정 문제들을 삭제
     *
     * @param requestDto 삭제할 목록 스케줄 조회 및 삭제할 문제번호
     */
    @Transactional
    public int deleteStudyProblems(DeleteScheduleProblemRequestDto requestDto) {
        // StudyProblem
        Long studyId = requestDto.getStudyId();
        Long problemId = requestDto.getProblemId();
        LocalDate deadline = requestDto.getDeadline();
        Study study = studyRepository.findById(requestDto.getStudyId())
                .orElseThrow(() -> new EntityNotFoundException("Study not found for studyId: " + studyId));
        study.getStudyProblems().removeIf(studyProblem ->
                studyProblem.getDeadline().equals(deadline) &&
                        studyProblem.getProblem().getProblemId().equals(problemId)
        );

        // StudySchedule
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
        // StudyProblem
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("Study not found for studyId: " + studyId));
        study.getStudyProblems().removeIf(studyProblem ->
                studyProblem.getDeadline().equals(deadline)
        );

        // StudySchedule
        StudySchedule studySchedule = studyScheduleRepository
                .findByStudyIdAndDeadline(studyId, deadline)
                .orElseThrow(() -> new EntityNotFoundException("Study schedule not found for studyId: " + studyId + " and deadline: " + deadline));

        studyScheduleRepository.delete(studySchedule);

        return 1;
    }

    @Transactional(readOnly = true)
    public DeadlinesResponseDto findDeadlines(Long studyId, LocalDate yearMonth){
        YearMonth currentYearMonth = YearMonth.from(yearMonth);
        LocalDate startOfMonth = currentYearMonth.atDay(1);
        LocalDate endOfMonth = currentYearMonth.atEndOfMonth();

        List<StudySchedule> studySchedules = studyScheduleRepository
                .findAllByStudyIdAndDeadlineBetween(studyId, startOfMonth, endOfMonth);

        List<LocalDate> deadlines = studySchedules.stream()
                .map(StudySchedule::getDeadline)
                .distinct()
                .collect(Collectors.toList());

        return DeadlinesResponseDto.builder()
                .studyId(studyId)
                .deadlines(deadlines)
                .build();
    }

    @Transactional(readOnly = true)
    public StudyScheduleResponseDto findRecentStudySchedule(String username) {
        LocalDate today = LocalDate.now();

        StudySchedule recentSchedule = studyScheduleRepository.findFirstByDeadlineGreaterThanEqualOrderByDeadlineAsc(today)
                .orElseThrow(() -> new EntityNotFoundException("No future study schedules found"));

        return StudyScheduleResponseDto.builder()
                .studyId(recentSchedule.getStudyId())
                .deadline(recentSchedule.getDeadline())
                .studyProblems(mapToStudyProblems(username, recentSchedule.getScheduleProblems(), recentSchedule.getStudyId()))
                .build();
    }


    //== mapper ==//
    private List<ScheduleProblemResponseDto> mapToStudyProblems(String username, List<ScheduleProblem> studyProblems, Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new EntityNotFoundException("Study schedule not found for studyId: " + studyId));
        int size = study.getMemberStudies().size();
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
                    return ScheduleProblemResponseDto.toDto(studyProblem, members, size, username);
                })
                .collect(Collectors.toList());
    }
}