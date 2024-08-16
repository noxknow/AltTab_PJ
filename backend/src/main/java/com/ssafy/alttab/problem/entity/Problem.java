package com.ssafy.alttab.problem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Problem {

    @Id
    @Column(name = "problem_id")
    private Long problemId;

    private String title;

    private String tag;

    private Long level;

    private String representative;

    private String problemIdTitle;
}
