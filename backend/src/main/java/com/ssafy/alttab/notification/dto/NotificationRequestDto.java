package com.ssafy.alttab.notification.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationRequestDto {

    private Long notificationId;
    private Long studyId;
    private boolean check;
}
