package com.ssafy.alttab.problem.entity;

import com.ssafy.alttab.study.entity.Study;
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
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long id;

    private Long memberId;
    private Long studyId;
    private Long problemId;

    public static Status createStatus(Long mId, Long sId, Long pId) {
        return Status.builder()
                .memberId(mId)
                .studyId(sId)
                .problemId(pId)
                .build();
    }

}
