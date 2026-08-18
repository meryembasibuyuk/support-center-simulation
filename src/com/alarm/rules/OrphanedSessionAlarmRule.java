package com.alarm.rules;

import com.alarm.Alarm;
import com.alarm.AlarmRule;
import com.alarm.AlarmSeverity;
import com.event.AgentOfflineEvent;
import com.event.DomainEvent;
import com.model.Agent;
import com.model.Session;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrphanedSessionAlarmRule implements AlarmRule {
    @Override
    public Optional<Alarm> evaluate(DomainEvent event) {
        if (event instanceof AgentOfflineEvent) {
            AgentOfflineEvent offlineEvent = (AgentOfflineEvent) event;
            Agent agent = offlineEvent.getAgent();

            // Null kontrolu: agent yoksa aktif session listesine erisilemez.
            if (agent == null) {
                return Optional.empty();
            }

            List<Session> activeSessions = agent.getActiveSessions();
            if (activeSessions == null || activeSessions.isEmpty()) {
                return Optional.empty();
            }

            StringBuilder sessionIds = new StringBuilder();
            for (Session s : activeSessions) {
                if (s != null) {
                    sessionIds.append(s.getSessionId()).append(" ");
                }
            }

            return Optional.of(new Alarm(
                    UUID.randomUUID().toString(),
                    AlarmSeverity.CRITICAL,
                    String.format("%s %s offline oldu ama %d aktif session sahipsiz kaldi: %s",
                            agent.getName(), agent.getSurname(), activeSessions.size(), sessionIds.toString().trim()),
                    "AgentOfflineEvent",
                    offlineEvent.occurredAt()
            ));
        }
        return Optional.empty();
    }
}