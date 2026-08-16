package com.event;

import java.time.Instant;

public abstract class AbstractEvent implements DomainEvent {
    private final Instant occurredAt = Instant.now();

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}
