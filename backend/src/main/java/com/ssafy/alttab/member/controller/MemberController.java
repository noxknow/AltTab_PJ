package com.ssafy.alttab.member.controller;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.member.dto.MemberInfoResponseDto;
import com.ssafy.alttab.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info/{name}")
    public ResponseEntity<?> getInfo(@PathVariable String name) throws MemberNotFoundException {
        return new ResponseEntity<>(memberService.getMemberInfo(name), HttpStatus.OK);
    }

}
