package com.ssafy.alttab.executor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeExecutionRequestDto {
    private Long studyId;
    private Long problemId;
    private Long memberId;
    private String input;
    private String code;
    private String runUsername;

    public void changeUserDetails(String runUsername){
        this.runUsername = runUsername;
    }
}
