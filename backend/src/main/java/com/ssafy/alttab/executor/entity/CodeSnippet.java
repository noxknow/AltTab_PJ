package com.ssafy.alttab.executor.entity;

import com.ssafy.alttab.executor.enums.ExecutionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSnippet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_snippet_id")
    private Long id;

    @Column(name = "language")
    private String language;

    @Lob
    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "execution_status")
    private ExecutionStatus executionStatus;

    @Column(name = "study_id")
    private Long studyGroupId;

    @Column(name = "problem_id")
    private Long problemId;

    @Column(name = "member_id")
    private Long memberId;

    //==비즈니스 로직==//

    /**
     * 코드 변경
     * @param code
     */
    public void changeCode(String code){
        this.code = code;
    }

    /**
     * 상태 변경
     * @param executionStatus
     */
    public void changeExecutionStatus(ExecutionStatus executionStatus){
        this.executionStatus = executionStatus;
    }

}
