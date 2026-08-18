package com.alarm.rules;

import com.alarm.Alarm;
import com.alarm.AlarmRule;
import com.alarm.AlarmSeverity;
import com.event.DomainEvent;
import com.event.SessionStartedEvent;
import com.model.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class SlowSessionStartAlarmRule implements AlarmRule {
    private final Duration threshold;

    public SlowSessionStartAlarmRule(Duration threshold) {
        this.threshold = threshold;
    }

    @Override
    public Optional<Alarm> evaluate(DomainEvent event) {
        if (event instanceof SessionStartedEvent) {
            SessionStartedEvent startedEvent = (SessionStartedEvent) event;
            Session session = startedEvent.getSession();
            Instant createdAt = session.getCreatedAt();
            Instant publishedAt = startedEvent.occurredAt();
            Duration delay = Duration.between(createdAt, publishedAt);

            if (delay.compareTo(threshold) > 0) {
                return Optional.of(new Alarm(
                        UUID.randomUUID().toString(),
                        AlarmSeverity.WARNING,
                        String.format("Session %s baslatilmasi %d ms gecikti",
                                session.getSessionId(), delay.toMillis()),
                        "SessionStartedEvent",
                        publishedAt
                ));
            }
        }
        return Optional.empty();
    }
}