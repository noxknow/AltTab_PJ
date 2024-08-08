package com.ssafy.alttab.solution.dto;

import com.ssafy.alttab.solution.document.Block;
import lombok.Getter;

import java.util.List;

@Getter
public class SolutionRequestDto {
    List<Block> blocks;
}
