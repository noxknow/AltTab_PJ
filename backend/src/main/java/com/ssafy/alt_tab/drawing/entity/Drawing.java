package com.ssafy.alt_tab.drawing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Drawing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drawingId;

    @Lob
    @Column(name = "drawing_data", columnDefinition = "LONGTEXT")
    private String drawingData;
}
