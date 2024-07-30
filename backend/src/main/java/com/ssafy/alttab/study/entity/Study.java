package com.ssafy.alttab.study.entity;

import com.ssafy.alttab.joinTable.entity.MemberStudy;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long studyId;

    private String studyName;
    private String studyInfo;

    @OneToMany(mappedBy = "study")
    private List<MemberStudy> memberStudyList;

}
