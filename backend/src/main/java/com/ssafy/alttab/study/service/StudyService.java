package com.ssafy.alttab.study.service;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.common.jointable.repository.MemberStudyRepository;
import com.ssafy.alttab.member.dto.MemberInfoResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.member.service.MemberService;
import com.ssafy.alttab.study.dto.StudyInfoResponseDto;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.entity.Study;
import com.ssafy.alttab.study.repository.StudyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.alttab.member.enums.MemberRoleStatus.FOLLOWER;

@Service
@RequiredArgsConstructor
public class StudyService {

    //    private final JavaMailSender mailSender;
    //    private final MemberStudyRepository memberStudyRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MemberStudyRepository memberStudyRepository;

//    private void sendInvitationEmail(String to, String studyName) {
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        try {
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
//            mimeMessageHelper.setTo(to);
//            mimeMessageHelper.setSubject(studyName + " 스터디 초대");
//            mimeMessageHelper.setText("안녕하세요, " + studyName + " 스터디에 초대되었습니다.");
//            mailSender.send(mimeMessage);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Transactional
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

    public StudyInfoResponseDto updateStudyInfo(Long studyId, StudyInfoRequestDto dto) throws StudyNotFoundException {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        study.updateStudy(dto);
        return StudyInfoResponseDto.builder()
                .studyName(study.getStudyName())
                .studyDescription(study.getStudyDescription())
                .build();
    }

    public Void followStudy(String name, Long studyId) throws MemberNotFoundException, StudyNotFoundException {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new MemberNotFoundException(name));
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(studyId));
        MemberStudy memberStudy = MemberStudy.createMemberStudy(member, study, FOLLOWER);
        memberStudyRepository.save(memberStudy);
        member.getMemberStudies().add(memberStudy);
        return null;
    }

    public Void unfollowStudy(String name, Long studyId) throws EntityNotFoundException {
        MemberStudy memberStudy = memberStudyRepository.findByMember_NameAndStudy_IdAndRole(name, studyId, FOLLOWER)
                .orElseThrow(() -> new EntityNotFoundException("MemberStudy not found for member name: " + name + ", study id: " + studyId + ", and role: FOLLOWER"));
        memberStudy.getMember().removeMemberStudy(memberStudy);
        memberStudyRepository.delete(memberStudy);
        return null;
    }
}