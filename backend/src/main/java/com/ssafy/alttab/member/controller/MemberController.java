package com.ssafy.alttab.member.controller;

import com.ssafy.alttab.member.dto.MemberDto;
import com.ssafy.alttab.member.service.MemberService;
import com.ssafy.alttab.security.oauth2.service.OAuth2Service;
import com.ssafy.alttab.study.entity.StudyInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final OAuth2Service oAuth2Service;

    @GetMapping
    public ResponseEntity<MemberDto> getMemberByUsername(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(memberService.getMemberByUsername(userDetails.getUsername()));
    }

    @PutMapping
    public ResponseEntity<MemberDto> updateMember(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MemberDto memberDto) {
        System.out.println("memberDto = " + memberDto);
        if (memberService.updateMember(userDetails.getUsername(), memberDto)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(500).build(); // Should never reach here
        }
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> deleteMemberAndLogout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            memberService.deleteMember(username);
            oAuth2Service.logout(request, response, username);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/studies")
    public ResponseEntity<List<StudyInfo>> getMemberStudies(@AuthenticationPrincipal UserDetails userDetails) {
        List<StudyInfo> studies = memberService.getMemberStudies(userDetails.getUsername());
        return ResponseEntity.ok(studies);
    }

}
