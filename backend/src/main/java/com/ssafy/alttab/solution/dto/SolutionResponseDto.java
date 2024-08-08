package com.ssafy.alttab.solution.dto;

import com.ssafy.alttab.solution.document.Block;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SolutionResponseDto {
    List<Block> blocks;
}
