package com.ssafy.alt_tab.room.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class RoomService {

    private final AtomicLong roomIdGenerator = new AtomicLong(0);

    public ResponseEntity<Long> createRoom() {

        return ResponseEntity.ok().body(roomIdGenerator.incrementAndGet());
    }
}
