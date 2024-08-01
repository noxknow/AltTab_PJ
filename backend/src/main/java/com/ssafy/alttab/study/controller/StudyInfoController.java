package com.ssafy.alttab.study.controller;

import com.ssafy.alttab.member.dto.MemberDto;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.service.StudyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StudyInfoController {

    private final StudyInfoService studyInfoService;

    @PostMapping("/study")
    public ResponseEntity<String> createStudy(@RequestBody StudyInfoRequestDto studyInfoRequestDto) {

        return studyInfoService.createStudy(studyInfoRequestDto);
    }

    @GetMapping("/study/{studyId}/members")
    public ResponseEntity<List<MemberDto>> getMembersByStudy(@PathVariable Long studyId) {

        return studyInfoService.getMembersByStudy(studyId);
    }
}
