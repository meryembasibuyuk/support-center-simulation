package com.alarm.rules;

import com.alarm.Alarm;
import com.alarm.AlarmRule;
import com.alarm.AlarmSeverity;
import com.event.DomainEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;


public class EventFrequencyAlarmRule implements AlarmRule {
    private final Class<? extends DomainEvent> targetType;
    private final int maxCount;
    private final Duration window;
    private final ConcurrentLinkedQueue<Instant> occurrences = new ConcurrentLinkedQueue<>();

    public EventFrequencyAlarmRule(Class<? extends DomainEvent> targetType, int maxCount, Duration window) {
        this.targetType = targetType;
        this.maxCount = maxCount;
        this.window = window;
    }

    @Override
    public Optional<Alarm> evaluate(DomainEvent event) {
        if (!targetType.isInstance(event)) {
            return Optional.empty();
        }

        Instant now = event.occurredAt();
        occurrences.add(now);

        // Zaman penceresi disina cikan kayitlari temizle.
        Iterator<Instant> it = occurrences.iterator();
        while (it.hasNext()) {
            Instant t = it.next();
            if (Duration.between(t, now).compareTo(window) > 0) {
                it.remove();
            }
        }

        int count = occurrences.size();

        if (count >= maxCount) {
            return Optional.of(new Alarm(
                UUID.randomUUID().toString(),
                AlarmSeverity.CRITICAL,
                String.format("%s son %d saniyede %d kez tetiklendi",
                    targetType.getSimpleName(), window.getSeconds(), count),
                targetType.getSimpleName(),
                now
            ));
        }

        return Optional.empty();
    }
}