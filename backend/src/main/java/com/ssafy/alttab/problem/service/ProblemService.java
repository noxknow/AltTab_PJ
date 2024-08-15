package com.ssafy.alttab.problem.service;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.common.jointable.repository.MemberStudyRepository;
import com.ssafy.alttab.common.jointable.repository.StudyProblemRepository;
import com.ssafy.alttab.member.dto.MemberResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.problem.dto.*;
import com.ssafy.alttab.problem.entity.Problem;
import com.ssafy.alttab.problem.entity.Status;
import com.ssafy.alttab.problem.repository.ProblemRepository;
import com.ssafy.alttab.problem.repository.StatusRepository;
import com.ssafy.alttab.study.entity.Study;
import com.ssafy.alttab.study.repository.StudyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ssafy.alttab.problem.entity.Status.createStatus;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final StudyProblemRepository studyProblemRepository;
    private final ProblemRepository problemRepository;
    private final StudyRepository studyRepository;
    private final MemberStudyRepository memberStudyRepository;
    private final StatusRepository statusRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity<SearchProblemListDto> searchProblems(String problemInfo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Problem> problemPage = problemRepository.findByProblemIdTitleContainingOrdered(problemInfo, pageable);

        List<SearchProblemResponseDto> problems = problemPage.getContent().stream()
                .map(SearchProblemResponseDto::toDto)
                .collect(Collectors.toList());

        SearchProblemListDto responseDto = SearchProblemListDto.builder()
                .problems(problems)
                .currentPage(problemPage.getNumber())
                .totalItems(problemPage.getTotalElements())
                .totalPages(problemPage.getTotalPages())
                .build();

        return ResponseEntity.ok().body(responseDto);
    }


    public ProblemListResponseDto queryProblems(Long studyId, Long option, String target) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        List<ProblemResponseDto> problems = switch (option.intValue()) {
            case 1 -> {
                List<StudyProblem> studyProblems = studyProblemRepository.findByStudyAndProblemTagContaining(study, target);
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
            case 4 -> {
                List<StudyProblem> studyProblems = studyProblemRepository.findByStudyAndProblemTitleContaining(study, target);
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

    public void solveProblem(Long memberId, Long studyId, Long problemId) throws StudyNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("member not found"));
        MemberStudy memberStudy = memberStudyRepository.findByMemberIdAndStudyId(memberId, studyId)
                .orElseThrow(() -> new EntityNotFoundException("entity not found"));
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException("entity not found"));
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        memberStudy.incrementMemberPoint(problem.getLevel());
        memberStudyRepository.save(memberStudy);
        member.incrementTotalPoint(problem.getLevel());
        memberRepository.save(member);
        study.incrementStudyPoint(problem.getLevel());
        study.incrementSolveCount();
        studyRepository.save(study);
        statusRepository.save(createStatus(memberId, studyId, problemId));
    }

    public SolveMembersResponseDto statusProblem(Long studyId, Long problemId) {
        List<Status> statusList = statusRepository.findByStudyIdAndProblemId(studyId, problemId);

        List<MemberResponseDto> members = statusList.stream()
                .map(status -> memberRepository.findById(status.getMemberId())
                        .map(member -> MemberResponseDto.builder()
                                .name(member.getName())
                                .avatarUrl(member.getAvatarUrl())
                                .build())
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return SolveMembersResponseDto.builder()
                .members(members)
                .build();
    }
}
