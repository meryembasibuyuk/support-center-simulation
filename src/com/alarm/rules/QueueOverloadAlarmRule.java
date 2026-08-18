package com.alarm.rules;

import com.alarm.Alarm;
import com.alarm.AlarmRule;
import com.alarm.AlarmSeverity;
import com.event.DomainEvent;
import com.event.QueueOverloadEvent;

import java.util.Optional;
import java.util.UUID;


public class QueueOverloadAlarmRule implements AlarmRule {
    @Override
    public Optional<Alarm> evaluate(DomainEvent event) {
        if (event instanceof QueueOverloadEvent) {
            QueueOverloadEvent overloadEvent = (QueueOverloadEvent) event;
            int size = overloadEvent.getQueueSize();
            int threshold = overloadEvent.getThreshold();

            AlarmSeverity severity = (size >= threshold * 2) ? AlarmSeverity.CRITICAL : AlarmSeverity.WARNING;

            return Optional.of(new Alarm(
                    UUID.randomUUID().toString(),
                    severity,
                    String.format("Kuyruk boyutu esigi asti: %d (esik: %d)", size, threshold),
                    "QueueOverloadEvent",
                    overloadEvent.occurredAt()
            ));
        }
        return Optional.empty();
    }
}
