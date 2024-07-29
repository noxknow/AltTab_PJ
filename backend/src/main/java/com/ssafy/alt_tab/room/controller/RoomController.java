package com.ssafy.alt_tab.room.controller;

import com.ssafy.alt_tab.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/api/v1/rooms")
    public ResponseEntity<Long> createRoom() {

        return roomService.createRoom();
    }
}