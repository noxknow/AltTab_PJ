package com.ssafy.alttab.study.dto;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.study.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyInfoRequestDto {
    private String studyName;
    private String studyDescription;
    private Long view;
    private Long like;
    private List<String> studyEmails;
    private List<MemberStudy> MemberStudies;
    private List<Problem> problems;
}
