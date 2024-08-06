package com.ssafy.alttab.study.dto;


import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.study.entity.Problem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyInfoResponseDto {
    private Long studyId;
    private String studyName;
    private String studyDescription;
    private Long view;
    private Long like;
    private List<String> studyEmails;
    private List<Problem> problems;
    private List<Member> studyMembers;
}
