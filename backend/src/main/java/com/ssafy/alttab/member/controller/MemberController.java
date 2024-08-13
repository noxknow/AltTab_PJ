package com.ssafy.alttab.member.controller;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.util.CookieUtil;
import com.ssafy.alttab.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    public ResponseEntity<?> getMemberInfo(@AuthenticationPrincipal UserDetails userDetails)
            throws MemberNotFoundException {
        return new ResponseEntity<>(memberService.getMemberInfo(userDetails.getUsername()), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(CookieUtil.deleteAuthTokens(request, response), HttpStatus.OK);
    }

    @GetMapping("/studies")
    public ResponseEntity<?> getJoinedStudies(@AuthenticationPrincipal UserDetails userDetails)
            throws MemberNotFoundException {
        return new ResponseEntity<>(memberService.getJoinedStudies(userDetails.getUsername()), HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> searchMember(@PathVariable String name) {
        return new ResponseEntity<>(memberService.searchMember(name), HttpStatus.OK);
    }

}
