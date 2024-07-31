package com.ssafy.alttab.study.controller;

import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.service.StudyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyInfoController {

    private final StudyInfoService studyInfoService;

    @PostMapping("/api/v2/study/{studyId}")
    public ResponseEntity<String> createStudy(@PathVariable("studyId") Long studyId, @RequestBody StudyInfoRequestDto studyInfoRequestDto) {

        return studyInfoService.createStudy(studyInfoRequestDto);
    }
}
