package com.ssafy.alttab.member.controller;

import com.ssafy.alttab.member.dto.MemberRequestDto;
import com.ssafy.alttab.member.dto.MemberResponseDto;
import com.ssafy.alttab.member.service.MemberService;
import com.ssafy.alttab.study.dto.StudyInfoResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v2/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponseDto> getMemberByUsername(@AuthenticationPrincipal UserDetails userDetails) {
        return memberService.getMemberByUsername(userDetails.getUsername());
    }

    @PutMapping
    public ResponseEntity<Void> updateMember(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MemberRequestDto memberDto) {
        return memberService.updateMember(userDetails.getUsername(), memberDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMemberAndLogout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) {
        return memberService.deleteMember(request, response, userDetails.getUsername());
    }

    @GetMapping("/studies")
    public ResponseEntity<List<StudyInfoResponseDto>> getMemberStudies(@AuthenticationPrincipal UserDetails userDetails) {
        return memberService.getMemberStudies(userDetails.getUsername());
    }

}
