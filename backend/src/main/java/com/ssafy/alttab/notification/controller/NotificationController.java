package com.ssafy.alttab.notification.controller;

import com.ssafy.alttab.common.exception.MemberNotFoundException;
import com.ssafy.alttab.common.exception.StudyNotFoundException;
import com.ssafy.alttab.notification.dto.NotificationRequestDto;
import com.ssafy.alttab.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/count")
    public ResponseEntity<?> countNotifications(@AuthenticationPrincipal UserDetails userDetails) throws MemberNotFoundException {

        return new ResponseEntity<>(notificationService.countNotifications(userDetails.getUsername()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getNotifications(@AuthenticationPrincipal UserDetails userDetails) throws MemberNotFoundException {

        return new ResponseEntity<>(notificationService.getNotifications(userDetails.getUsername()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> checkNotifications(@AuthenticationPrincipal UserDetails userDetails, @RequestBody NotificationRequestDto notificationRequestDto) throws MemberNotFoundException, StudyNotFoundException {

        notificationService.checkNotification(userDetails.getUsername(), notificationRequestDto);
        return ResponseEntity.ok().build();
    }
}
