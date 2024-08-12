package com.ssafy.alttab.notification.dto;

import com.ssafy.alttab.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {

    private Long notificationId;
    private Long studyId;
    private String studyName;

    public static NotificationResponseDto toDto(Notification notification) {
        return NotificationResponseDto.builder()
                .notificationId(notification.getId())
                .studyId(notification.getStudyId())
                .studyName(notification.getStudyName())
                .build();
    }
}
