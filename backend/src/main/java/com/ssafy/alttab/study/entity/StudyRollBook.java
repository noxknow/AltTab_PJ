package com.ssafy.alttab.study.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRollBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_roll_book_id")
    private Long id;

    private String memberName;
    private Long studyId;
    private LocalDate todayDate;

    public static StudyRollBook createStudyRollBook(String name, Long studyId, LocalDate todayDate) {
        return StudyRollBook.builder()
                .memberName(name)
                .studyId(studyId)
                .todayDate(todayDate)
                .build();
    }
}
