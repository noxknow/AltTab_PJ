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
public class StudyInfoController {

    private final StudyInfoService studyInfoService;

    @PostMapping("/api/v2/study/{studyId}")
    public ResponseEntity<String> createStudy(@PathVariable("studyId") Long studyId, @RequestBody StudyInfoRequestDto studyInfoRequestDto) {

        return studyInfoService.createStudy(studyInfoRequestDto);
    }

    @GetMapping("/api/v2/study/{studyId}/members")
    public ResponseEntity<List<MemberDto>> getMembersByStudy(@PathVariable Long studyId) {
        List<MemberDto> members = studyInfoService.getMembersByStudy(studyId);
        return ResponseEntity.ok(members);
    }
}
