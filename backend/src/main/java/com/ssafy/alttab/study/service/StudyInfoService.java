package com.ssafy.alttab.study.service;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.common.jointable.repository.MemberStudyRepository;
import com.ssafy.alttab.member.dto.MemberDto;
import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import com.ssafy.alttab.study.entity.StudyInfo;
import com.ssafy.alttab.study.repository.StudyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyInfoService {

    private final StudyInfoRepository studyInfoRepository;
    private final MemberStudyRepository memberStudyRepository;

    public ResponseEntity<String> createStudy(StudyInfoRequestDto studyInfoRequestDto) {

        StudyInfo studyInfo = StudyInfo.createStudy(studyInfoRequestDto);
        studyInfoRepository.save(studyInfo);

        return ResponseEntity.ok().body("Study created successfully");
    }

    public ResponseEntity<List<MemberDto>> getMembersByStudy(Long studyId) {

        StudyInfo studyInfo = findStudyByIdOrThrow(studyId);
        List<MemberStudy> memberStudies = memberStudyRepository.findByStudyInfo(studyInfo);

        return ResponseEntity.ok().body(memberStudies.stream()
                .map(memberStudy -> MemberDto.fromEntity(memberStudy.getMember()))
                .collect(Collectors.toList()));
    }

    private StudyInfo findStudyByIdOrThrow(Long studyId) {

        return studyInfoRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));
    }
}
