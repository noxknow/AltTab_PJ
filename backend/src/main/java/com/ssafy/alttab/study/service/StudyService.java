package com.ssafy.alttab.study.service;

import static com.ssafy.alttab.common.jointable.entity.MemberStudy.createMemberStudy;
import static com.ssafy.alttab.member.dto.MemberInfoResponseDto.toDto;
import static com.ssafy.alttab.member.enums.MemberRoleStatus.FOLLOWER;
import static com.ssafy.alttab.member.enums.MemberRoleStatus.LEADER;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.common.jointable.repository.StudyProblemRepository;
import com.ssafy.alttab.member.dto.MemberInfoResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.notification.service.NotificationService;
import com.ssafy.alttab.study.dto.*;
import com.ssafy.alttab.study.entity.Study;
import com.ssafy.alttab.study.repository.StudyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final StudyProblemRepository studyProblemRepository;

    public StudyInfoResponseDto getStudyInfo(Long studyId) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        return StudyInfoResponseDto.toDto(study);
    }

    public List<MemberInfoResponseDto> getStudyMembers(Long studyId) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        return study.getMemberStudies().stream()
                .filter(memberStudy -> !memberStudy.getRole().equals(FOLLOWER))
                .map(memberStudy -> toDto(memberStudy.getMember(), memberStudy.getMemberPoint()))
                .collect(Collectors.toList());
    }

    @Transactional
    public StudyInfoResponseDto updateStudyInfo(Long studyId, StudyInfoRequestDto dto) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        study.updateStudy(dto);
        return StudyInfoResponseDto.toDto(study);
    }

    @Transactional
    public StudyIdResponseDto createStudy(String username, MakeStudyRequestDto dto) throws MemberNotFoundException {
        Study study = Study.createStudy(dto.getStudyName(), dto.getStudyDescription());
        Member member = memberRepository.findByName(username)
                .orElseThrow(() -> new MemberNotFoundException(username));
        MemberStudy memberStudy = createMemberStudy(member, study, LEADER);
        study.addMemberStudy(memberStudy);
        studyRepository.save(study);
        List<Long> memberIds = dto.getMemberIds();
        for (Long memberId : memberIds) {
            try {
                notificationService.createNotification(memberId, study.getId(), study.getStudyName());
            } catch (MemberNotFoundException e) {
                throw new MemberNotFoundException(memberId);
            }
        }
        return StudyIdResponseDto.builder().studyId(study.getId()).build();
    }

    public StudyScoreResponseDto getStudyScore(Long studyId) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        int rank = studyRepository.findStudyPointRankById(studyId);
        List<StudyProblem> studyProblemList = studyProblemRepository.findByStudyId(studyId);

        Map<String, Integer> tagIndexMap = new HashMap<>();
        tagIndexMap.put("수학", 0);
        tagIndexMap.put("다이나믹 프로그래밍", 1);
        tagIndexMap.put("자료 구조", 2);
        tagIndexMap.put("구현", 3);
        tagIndexMap.put("그래프 이론", 4);
        tagIndexMap.put("탐색", 5);
        tagIndexMap.put("정렬", 6);
        tagIndexMap.put("문자열", 7);

        int[] tagCount = new int[8];
        for (StudyProblem studyProblem : studyProblemList) {
            Integer index = tagIndexMap.get(studyProblem.getTag());
            if (index != null) {
                tagCount[index]++;
            }
        }

        return StudyScoreResponseDto.toDto(study, tagCount, rank);
    }
}