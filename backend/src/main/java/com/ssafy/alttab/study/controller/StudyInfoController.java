package com.ssafy.alttab.study.controller;

import com.ssafy.alttab.member.dto.MemberResponseDto;
import com.ssafy.alttab.study.dto.AddMembersToStudyDto;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.dto.StudyInfoResponseDto;
import com.ssafy.alttab.study.entity.StudyInfo;
import com.ssafy.alttab.study.service.StudyInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StudyInfoController {

    private final StudyInfoService studyInfoService;

    @PostMapping("/study")
    public ResponseEntity<Long> createStudy(@AuthenticationPrincipal UserDetails userDetails, @RequestBody StudyInfoRequestDto studyInfoRequestDto) {

        return studyInfoService.createStudy(studyInfoRequestDto);
    }

    @GetMapping("/study/{studyId}")
    public ResponseEntity<StudyInfo> loadStudyInfo(@PathVariable Long studyId) {
        return studyInfoService.loadStudyInfo(studyId);
    }

    @GetMapping("/study/{studyId}/members")
    public ResponseEntity<List<MemberResponseDto>> loadStudyMembers(@PathVariable Long studyId) {
        return studyInfoService.loadStudyMembers(studyId);
    }

    @PutMapping("/study/{studyId}")
    public ResponseEntity<Void> updateStudy(@PathVariable Long studyId, @RequestBody StudyInfoRequestDto studyInfoRequestDto) {
        return studyInfoService.updateStudy(studyId, studyInfoRequestDto);
    }

    @DeleteMapping("/study/{studyId}")
    public ResponseEntity<Void> deleteStudy(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long studyId) {
        return studyInfoService.deleteStudy(userDetails.getUsername(), studyId);
    }

    @DeleteMapping("/study/{studyId}/{memberId}")
    public ResponseEntity<Void> deleteStudyMember(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long studyId, @PathVariable Long memberId) {
        return studyInfoService.deleteStudyMember(userDetails.getUsername(), studyId, memberId);
    }

    @PostMapping("/study/members")
    public ResponseEntity<StudyInfoResponseDto> addMembersToStudy(@RequestBody AddMembersToStudyDto dto) {
        return studyInfoService.addMembersToStudy(dto.getStudyId(), dto.getMemberIds());
    }
}
