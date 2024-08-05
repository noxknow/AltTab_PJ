package com.ssafy.alttab.study.dto;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.study.entity.Problem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyInfoRequestDto {
    private String studyName;
    private String studyDescription;
    private List<String> studyEmails;
    private List<MemberStudy> MemberStudies;
    private List<Problem> problems;
}
