package com.ssafy.alttab.study.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AttendMembersResponse {
    private List<String> members;
}
