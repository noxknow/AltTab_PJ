package com.ssafy.alttab.compiler.entity;

import com.ssafy.alttab.compiler.enums.ExecutionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private String language;

    @Lob
    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Column(name = "execution_status")
    private ExecutionStatus executionStatus;

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
