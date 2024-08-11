package com.ssafy.alttab.problem.service;

import com.ssafy.alttab.common.exception.ProblemNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.common.jointable.repository.StudyProblemRepository;
import com.ssafy.alttab.problem.dto.*;
import com.ssafy.alttab.problem.entity.Problem;
import com.ssafy.alttab.problem.repository.ProblemRepository;
import com.ssafy.alttab.study.entity.Study;
import com.ssafy.alttab.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.alttab.common.jointable.entity.StudyProblem.createStudyProblem;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final StudyProblemRepository studyProblemRepository;
    private final ProblemRepository problemRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public void addProblems(Long studyId, AddProblemsRequestDto dto) throws StudyNotFoundException, ProblemNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        LocalDate deadline = dto.getDeadline();
        for (AddProblemRequestDto problemDto : dto.getProblemIds()) {
            Long problemId = problemDto.getProblemId();
            Problem problem = problemRepository.findById(problemId)
                    .orElseThrow(() -> new ProblemNotFoundException(problemId));
            study.addStudyProblem(createStudyProblem(study, problem, deadline, problemDto.getPresenter()));
        }
        studyRepository.save(study);
    }

    @Transactional
    public ProblemListResponseDto getProblems(Long studyId) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        return ProblemListResponseDto.builder()
                .problemList(studyProblemRepository.findByStudyOrderByDeadlineDesc(study).stream()
                        .map(ProblemResponseDto::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void removeProblems(Long studyId, RemoveProblemsRequestDto dto) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        List<Long> psIds = dto.getProblemStudyIds();
        studyProblemRepository.deleteByStudyIdAndIdIn(studyId, psIds);
        study.getStudyProblems().removeIf(studyProblem -> psIds.contains(studyProblem.getId()));
        studyRepository.save(study);
    }

    public ProblemListResponseDto queryProblems(Long studyId, Long option, String target) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        List<ProblemResponseDto> problems = switch (option.intValue()) {
            case 1 -> {
                List<StudyProblem> studyProblems = studyProblemRepository.findByStudyAndTag(study, target);
                yield studyProblems.stream()
                        .map(ProblemResponseDto::toDto)
                        .collect(Collectors.toList());
            }
            case 2 -> {
                List<StudyProblem> studyProblems = studyProblemRepository.findByStudyAndLevel(study, Long.valueOf(target));
                yield studyProblems.stream()
                        .map(ProblemResponseDto::toDto)
                        .collect(Collectors.toList());
            }
            case 3 -> {
                List<StudyProblem> studyProblems = studyProblemRepository.findByStudyAndPresenter(study, target);
                yield studyProblems.stream()
                        .map(ProblemResponseDto::toDto)
                        .collect(Collectors.toList());
            }
            default -> List.of();
        };
        return ProblemListResponseDto.builder()
                .problemList(problems)
                .build();
    }

    public ProblemListResponseDto weeklyProblems(Long studyId, LocalDate today) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        return ProblemListResponseDto.builder()
                .problemList(studyProblemRepository.findByStudyAndDeadlineGreaterThanEqualOrderByDeadlineAsc(study, today)
                        .stream()
                        .map(ProblemResponseDto::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
