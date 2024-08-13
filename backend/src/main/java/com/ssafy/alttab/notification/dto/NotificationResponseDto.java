package com.ssafy.alttab.notification.dto;

import com.ssafy.alttab.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {

    private Long notificationId;
    private Long studyId;
    private String studyName;
    private LocalDateTime createdAt;

    public static NotificationResponseDto toDto(Notification notification) {
        return NotificationResponseDto.builder()
                .notificationId(notification.getId())
                .studyId(notification.getStudyId())
                .studyName(notification.getStudyName())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
