package com.alarm;

import com.event.DomainEvent;
import java.util.Optional;

public interface AlarmRule {
    Optional<Alarm> evaluate(DomainEvent event);
}