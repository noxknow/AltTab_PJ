package com.ssafy.alttab.study.service;

import static com.ssafy.alttab.common.jointable.entity.MemberStudy.createMemberStudy;
import static com.ssafy.alttab.member.enums.MemberRoleStatus.FOLLOWER;
import static com.ssafy.alttab.member.enums.MemberRoleStatus.LEADER;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.dto.MemberInfoResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.notification.service.NotificationService;
import com.ssafy.alttab.study.dto.MakeStudyRequestDto;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.dto.StudyInfoResponseDto;
import com.ssafy.alttab.study.entity.Study;
import com.ssafy.alttab.study.repository.StudyRepository;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public StudyInfoResponseDto getStudyInfo(Long studyId) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        return StudyInfoResponseDto.builder()
                .studyName(study.getStudyName())
                .studyDescription(study.getStudyDescription())
                .build();
    }

    public List<MemberInfoResponseDto> getStudyMembers(Long studyId) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        return study.getMemberStudies().stream()
                .filter(memberStudy -> !memberStudy.getRole().equals(FOLLOWER))
                .map(MemberStudy::getMember)
                .map(MemberInfoResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudyInfoResponseDto updateStudyInfo(Long studyId, StudyInfoRequestDto dto) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        study.updateStudy(dto);
        return StudyInfoResponseDto.builder()
                .studyName(study.getStudyName())
                .studyDescription(study.getStudyDescription())
                .build();
    }

    @Transactional
    public void createStudy(String username, MakeStudyRequestDto dto) throws MemberNotFoundException {
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
    }
}