package com.ssafy.alttab.drawing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrawingRequestDto {

    private Long studyId;
    private Long problemId;
    private String drawingData;
}