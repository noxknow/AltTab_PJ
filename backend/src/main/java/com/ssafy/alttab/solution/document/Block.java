package com.ssafy.alttab.solution.document;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Block {
    private String id;
    private String text;
    private String option;
}