package com.ssafy.alt_tab.drawing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrawingRequestDto {

    private Long id;
    private String drawingData;
}
