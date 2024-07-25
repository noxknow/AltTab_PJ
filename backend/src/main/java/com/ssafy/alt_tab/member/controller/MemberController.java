package com.ssafy.alt_tab.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/v2/member")
@RequiredArgsConstructor
public class MemberController {

//    @GetMapping("/login")
    @GetMapping("/")
    public String login() {
        return "login";
    }
}
