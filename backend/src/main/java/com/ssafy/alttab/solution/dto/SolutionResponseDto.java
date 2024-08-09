package com.ssafy.alttab.solution.dto;

import com.ssafy.alttab.solution.document.Block;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SolutionResponseDto {
    List<Block> blocks;
}
