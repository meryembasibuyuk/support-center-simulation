package com.alarm;

import java.time.Instant;
import java.util.Objects;

public class Alarm {
    private final String id;
    private final AlarmSeverity severity;
    private final String message;
    private final String sourceEvent;
    private final Instant occurredAt;

    public Alarm(String id, AlarmSeverity severity, String message, String sourceEvent, Instant occurredAt) {
        this.id = id;
        this.severity = severity;
        this.message = message;
        this.sourceEvent = sourceEvent;
        this.occurredAt = occurredAt;
    }

    public String getId() { return id; }
    public AlarmSeverity getSeverity() { return severity; }
    public String getMessage() { return message; }
    public String getSourceEvent() { return sourceEvent; }
    public Instant getOccurredAt() { return occurredAt; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (kaynak: %s, zaman: %s)",
                severity, id, message, sourceEvent, occurredAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alarm alarm = (Alarm) o;
        return Objects.equals(id, alarm.id)
                && severity == alarm.severity
                && Objects.equals(message, alarm.message)
                && Objects.equals(sourceEvent, alarm.sourceEvent)
                && Objects.equals(occurredAt, alarm.occurredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, severity, message, sourceEvent, occurredAt);
    }
}