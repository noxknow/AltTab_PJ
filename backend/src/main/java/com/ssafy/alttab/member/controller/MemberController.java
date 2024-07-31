package com.ssafy.alttab.member.controller;

import com.ssafy.alttab.member.dto.MemberDto;
import com.ssafy.alttab.member.service.MemberService;
import com.ssafy.alttab.security.redis.service.BlackListService;
import com.ssafy.alttab.security.redis.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.ssafy.alttab.security.jwt.JWTUtil.findCookie;
import static com.ssafy.alttab.security.oauth2.controller.OAuth2Controller.removeCookie;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final BlackListService blackListService;
    private final TokenService tokenService;

    @GetMapping
    public ResponseEntity<MemberDto> getMemberByUsername(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(memberService.getMemberByUsername(userDetails.getUsername()));
    }

    @PutMapping
    public ResponseEntity<MemberDto> updateMember(@AuthenticationPrincipal UserDetails userDetails, MemberDto memberDto) {
//        return ResponseEntity.ok(memberService.updateMember(userDetails.getUsername(), memberDto));
        System.out.println(memberDto);
        if (memberService.updateMember(userDetails.getUsername(), memberDto)) {
            return ResponseEntity.ok().build(); // HTTP 200 OK with no body
        } else {
            return ResponseEntity.status(500).build(); // Should never reach here
        }
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteMemberAndLogout(@AuthenticationPrincipal UserDetails userDetails,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        String accessToken = findCookie("Access-Token", request);

        if (memberService.deleteMemberAndLogout(userDetails.getUsername(), accessToken)) {
            removeCookie(response); // Remove cookie if necessary
            blackListService.addToBlacklist(accessToken);
            tokenService.deleteRefreshToken(userDetails.getUsername());
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } else {
            return ResponseEntity.status(500).build(); // Should never reach here
        }
    }
}
