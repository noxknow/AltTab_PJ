package com.ssafy.alttab.study.entity;

import com.ssafy.alttab.study.dto.StudyInfoRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studyId")
    private Long Id;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String studyName;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String studyDescription;

    @ElementCollection
    @CollectionTable(name = "study_emails", joinColumns = @JoinColumn(name = "study_id"))
    @Column(name = "email")
    private List<String> studyEmails;

    public static StudyInfo createStudy(StudyInfoRequestDto studyInfoRequestDto) {

        return StudyInfo.builder()
                .studyName(studyInfoRequestDto.getStudyName())
                .studyDescription(studyInfoRequestDto.getStudyDescription())
                .studyEmails(studyInfoRequestDto.getStudyEmails())
                .build();
    }
}