package com.ssafy.alttab.study.controller;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.dto.MemberDto;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.entity.StudyInfo;
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

    @GetMapping("/study/{studyId}")
    public ResponseEntity<StudyInfo> loadStudyInfo(@PathVariable Long studyId) {
        System.out.println(studyId);
        return studyInfoService.loadStudyInfo(studyId);
    }

//    @GetMapping("/study/{studyId}")
//    public ResponseEntity<List<MemberStudy>> loadMembersByStudy(@PathVariable Long studyId) {
//
//        return studyInfoService.loadMembersByStudy(studyId);
//    }
}
