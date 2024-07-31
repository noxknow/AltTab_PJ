package com.ssafy.alttab.study.service;

import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.entity.StudyInfo;
import com.ssafy.alttab.study.repository.StudyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyInfoService {

    private final StudyInfoRepository studyInfoRepository;

    public ResponseEntity<String> createStudy(StudyInfoRequestDto studyInfoRequestDto) {

        StudyInfo studyInfo = StudyInfo.createStudy(studyInfoRequestDto);
        studyInfoRepository.save(studyInfo);

        return ResponseEntity.ok().body("Study created successfully");
    }
}
