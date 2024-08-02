package com.ssafy.alttab.common.realtime.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface RealtimeUpdateService<T> {
    SseEmitter subscribe();
    void sendUpdate(T data);
}