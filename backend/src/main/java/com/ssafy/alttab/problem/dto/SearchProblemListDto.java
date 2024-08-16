package com.ssafy.alttab.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SearchProblemListDto {

    private List<SearchProblemResponseDto> problems;
    private int currentPage;
    private long totalItems;
    private int totalPages;
}
