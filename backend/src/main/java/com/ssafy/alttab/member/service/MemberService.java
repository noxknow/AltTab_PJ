package com.ssafy.alttab.member.service;

import com.ssafy.alttab.member.dto.MemberRequestDto;
import com.ssafy.alttab.member.dto.MemberResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.repository.MemberRepository;
import com.ssafy.alttab.security.oauth2.service.OAuth2Service;
import com.ssafy.alttab.study.dto.StudyInfoResponseDto;
import com.ssafy.alttab.study.entity.StudyInfo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final OAuth2Service oAuth2Service;

    public ResponseEntity<MemberResponseDto> getMemberByUsername(String username) {
        try {
            Member member = findByUsernameOrThrow(username);
            return ResponseEntity.ok(member.toDto());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<Void> updateMember(String username, MemberRequestDto memberDto) {
        try {
            Member member = findByUsernameOrThrow(username);
            member.fromDto(memberDto);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteMember(HttpServletRequest request, HttpServletResponse response, String username) {
        try {
            Member member = findByUsernameOrThrow(username);
            memberRepository.delete(member);
            oAuth2Service.logout(request, response, username);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<StudyInfoResponseDto>> getMemberStudies(String username) {
        try {
            Member member = findByUsernameOrThrow(username);
            List<StudyInfoResponseDto> studyInfoList = member.getMemberStudies().stream()
                    .map(memberStudy -> {
                        StudyInfo studyInfo = memberStudy.getStudyInfo();
                        return StudyInfoResponseDto.builder()
                                .studyId(studyInfo.getId())
                                .studyName(studyInfo.getStudyName())
                                .studyEmails(studyInfo.getStudyEmails())
                                .studyDescription(studyInfo.getStudyDescription())
                                .build();
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(studyInfoList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    public Member findByUsernameOrThrow(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with username: " + username));
    }

    public Member findByMemberEmailOrElse(String email) {
        return memberRepository.findByMemberEmail(email)
                .orElse(null);
    }
}
