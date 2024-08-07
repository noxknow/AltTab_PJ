package com.ssafy.alttab.member.service;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.member.dto.MemberInfoResponseDto;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member saveOrUpdateMember(String name, String avatarUrl) {
        return memberRepository.findByName(name)
                .map(member -> {
                    member.changeAvatarUrl(avatarUrl);
                    return memberRepository.save(member);
                })
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .name(name)
                        .avatarUrl(avatarUrl)
                        .role(MemberRoleStatus.MEMBER)
                        .build()));
    }

    @Transactional
    public MemberInfoResponseDto getMemberInfo(String name) throws MemberNotFoundException{
        Optional<Member> member = memberRepository.findByName(name);

        if(member.isPresent()){
            return MemberInfoResponseDto.builder()
                    .name(member.get().getName())
                    .avatarUrl(member.get().getAvatarUrl())
                    .build();
        }

        throw new MemberNotFoundException(name);
    }

//    public ResponseEntity<MemberResponseDto> getMember(String name) {
//        try {
//            Member member = findByUsernameOrThrow(name);
//            return ResponseEntity.ok(member.toDto());
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @Transactional
//    public ResponseEntity<Void> updateMember(String username, MemberRequestDto memberDto) {
//        try {
//            Member member = findByUsernameOrThrow(username);
//            member.fromDto(memberDto);
//            return ResponseEntity.ok().build();
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @Transactional
//    public ResponseEntity<Void> deleteMember(HttpServletRequest request, HttpServletResponse response, String username) {
//        try {
//            Member member = findByUsernameOrThrow(username);
//            memberRepository.delete(member);
//            return ResponseEntity.ok().build();
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    public ResponseEntity<List<StudyInfoResponseDto>> getMemberStudies(String username) {
//        try {
//            Member member = findByUsernameOrThrow(username);
//            List<StudyInfoResponseDto> studyInfoList = member.getMemberStudies().stream()
//                    .map(memberStudy -> {
//                        StudyInfo studyInfo = memberStudy.getStudyInfo();
//                        return StudyInfoResponseDto.builder()
//                                .studyId(studyInfo.getId())
//                                .studyName(studyInfo.getStudyName())
//                                .studyEmails(studyInfo.getStudyEmails())
//                                .studyDescription(studyInfo.getStudyDescription())
//                                .build();
//                    })
//                    .collect(Collectors.toList());
//            return ResponseEntity.ok(studyInfoList);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build(); // 404 Not Found
//        }
//    }
//
//    public Member findByUsernameOrThrow(String name) {
//        return memberRepository.findByName(name)
//                .orElseThrow(() -> new EntityNotFoundException("Member not found with username: " + username));
//    }
}

