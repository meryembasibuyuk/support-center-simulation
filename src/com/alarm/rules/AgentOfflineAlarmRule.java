package com.alarm.rules;

import com.alarm.Alarm;
import com.alarm.AlarmRule;
import com.alarm.AlarmSeverity;
import com.event.AgentOfflineEvent;
import com.event.DomainEvent;
import com.model.Agent;

import java.util.Optional;
import java.util.UUID;

public class AgentOfflineAlarmRule implements AlarmRule {

    @Override
    public Optional<Alarm> evaluate(DomainEvent event) {
        if (event instanceof AgentOfflineEvent) {
            AgentOfflineEvent offlineEvent = (AgentOfflineEvent) event;
            Agent agent = offlineEvent.getAgent();

            // Null kontrolu: agent bilgisi olmadan alarm mesaji uretilemez.
            if (agent == null) {
                return Optional.empty();
            }

            String fullName = agent.getName() + " " + agent.getSurname();

            return Optional.of(new Alarm(
                UUID.randomUUID().toString(),
                AlarmSeverity.WARNING,
                String.format("Agent çevrimdışı oldu: %s (ID: %s)", fullName, agent.getAgentId()),
                "AgentOfflineEvent",
                offlineEvent.occurredAt()
            ));
        }
        return Optional.empty();
    }
}