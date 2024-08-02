package com.ssafy.alttab.common.realtime.service;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public abstract class AbstractRealtimeUpdateService<T> implements RealtimeUpdateService<T> {
    protected final ConcurrentHashMap<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String emitterId = UUID.randomUUID().toString();
        sseEmitters.put(emitterId, emitter);

        emitter.onCompletion(() -> sseEmitters.remove(emitterId));
        emitter.onTimeout(() -> sseEmitters.remove(emitterId));

        try {
            emitter.send(SseEmitter.event().name("INIT").data(getInitialData()));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    @Override
    public void sendUpdate(T data) {
        sseEmitters.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("UPDATE").data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
    }

    protected abstract T getInitialData();
}