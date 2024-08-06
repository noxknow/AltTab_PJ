package com.ssafy.alttab.study.service;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.common.jointable.repository.MemberStudyRepository;
import com.ssafy.alttab.member.dto.MemberResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.member.service.MemberService;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.dto.StudyInfoResponseDto;
import com.ssafy.alttab.study.entity.StudyInfo;
import com.ssafy.alttab.study.repository.StudyInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.alttab.common.jointable.entity.MemberStudy.createMemberStudy;
import static com.ssafy.alttab.member.enums.MemberRoleStatus.LEADER;
import static com.ssafy.alttab.member.enums.MemberRoleStatus.TEAM_MEMBER;

@Service
@RequiredArgsConstructor
public class StudyInfoService {

    //    private final JavaMailSender mailSender;
    private final StudyInfoRepository studyInfoRepository;
    private final MemberRepository memberRepository;
    private final MemberStudyRepository memberStudyRepository;
    private final MemberService memberService;

    @Transactional
    public ResponseEntity<Long> createStudy(String username, StudyInfoRequestDto studyInfoRequestDto) {
        StudyInfo studyInfo = StudyInfo.createStudy(studyInfoRequestDto);
        studyInfoRepository.save(studyInfo);
        try {
            Member leader = memberService.findByUsernameOrThrow(username);
            List<String> emails = studyInfo.getStudyEmails();
            List<MemberStudy> memberStudies = studyInfo.getMemberStudies();
            // 1. 회원들 이메일, 회원-스터디 추가
            for (String email : studyInfoRequestDto.getStudyEmails()) {
                sendInvitationEmail(email, studyInfoRequestDto.getStudyName());
                Member teamMember = memberService.findByMemberEmailOrElse(email);
                if (teamMember != null) {
                    MemberStudy memberStudy = createMemberStudy(teamMember, studyInfo, TEAM_MEMBER);
                    memberStudyRepository.save(memberStudy);
                    memberStudies.add(memberStudy);
                    teamMember.getMemberStudies().add(memberStudy);
                }
            }
            // 2. 방장 이메일 추가
            emails.add(leader.getMemberEmail());
            // 3. 방장-스터디 추가
            MemberStudy leaderStudy = createMemberStudy(leader, studyInfo, LEADER);
            memberStudyRepository.save(leaderStudy);
            memberStudies.add(leaderStudy);
            leader.getMemberStudies().add(leaderStudy);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(studyInfo.getId());
    }

    private void sendInvitationEmail(String to, String studyName) {

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
    }

    @Transactional
    public ResponseEntity<StudyInfoResponseDto> loadStudyInfo(Long studyId) {
        try {
            StudyInfo studyInfo = findStudyByIdOrThrow(studyId);
            Hibernate.initialize(studyInfo.getStudyEmails());
            return ResponseEntity.ok(studyInfo.toDto());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<MemberResponseDto>> loadStudyMembers(Long studyId) {
        try {
            StudyInfo studyInfo = findStudyByIdOrThrow(studyId);
            List<MemberResponseDto> memberList = studyInfo.getMemberStudies().stream()
                    .map(MemberStudy::getMember)
                    .map(Member::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(memberList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteStudy(String username, Long studyId) {
        try {
            Member member = memberService.findByUsernameOrThrow(username);
            StudyInfo studyInfo = studyInfoRepository.findById(studyId)
                    .orElseThrow(() -> new EntityNotFoundException("Study not found with id: " + studyId));
            MemberStudy memberStudy = memberStudyRepository.findByMemberAndStudyInfo(member, studyInfo)
                    .orElseThrow(() -> new EntityNotFoundException("MemberStudy entry not found for the given member and study"));
            if (LEADER.equals(memberStudy.getRole())) {
                studyInfoRepository.delete(studyInfo);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteStudyMember(String username, Long studyId, Long memberId) {
        try {
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Member not found with username: " + username));
            StudyInfo studyInfo = studyInfoRepository.findById(studyId)
                    .orElseThrow(() -> new EntityNotFoundException("Study not found with id: " + studyId));
            MemberStudy memberStudy = memberStudyRepository.findByMemberAndStudyInfo(member, studyInfo)
                    .orElseThrow(() -> new EntityNotFoundException("MemberStudy entry not found for the given member and study"));
            if (LEADER.equals(memberStudy.getRole())) {
                memberStudyRepository.delete(memberStudy);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> updateStudy(Long studyId, StudyInfoRequestDto studyInfoRequestDto) {
        try {
            StudyInfo studyInfo = studyInfoRepository.findById(studyId)
                    .orElseThrow(() -> new EntityNotFoundException("Study not found with id: " + studyId));
            studyInfo.fromDto(studyInfoRequestDto);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<StudyInfoResponseDto> addMembersToStudy(Long studyId, List<String> emails) {
        // 1. 스터디 찾기
        StudyInfo studyInfo = studyInfoRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("Study not found"));
        // 2. 이메일로 멤버리스트 찾기
        List<Member> membersToAdd = memberRepository.findAllByMemberEmailIn(emails);
        List<MemberStudy> memberStudies = studyInfo.getMemberStudies();
        for (Member member : membersToAdd) {
            if (!memberStudyRepository.existsByMemberAndStudyInfo(member, studyInfo)) {
                MemberStudy memberStudy = MemberStudy.builder()
                        .member(member)
                        .studyInfo(studyInfo)
                        .role(MemberRoleStatus.TEAM_MEMBER) // 기본 역할 설정
                        .build();
                memberStudyRepository.save(memberStudy);
                memberStudies.add(memberStudy);
                member.getMemberStudies().add(memberStudy);
            }
        }
        return ResponseEntity.ok(studyInfo.toDto());
    }

    private StudyInfo findStudyByIdOrThrow(Long studyId) {

        return studyInfoRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("Study not found"));
    }
}
