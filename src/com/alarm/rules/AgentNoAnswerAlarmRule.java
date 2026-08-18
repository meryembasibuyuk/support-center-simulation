package com.alarm.rules;

import com.alarm.Alarm;
import com.alarm.AlarmRule;
import com.alarm.AlarmSeverity;
import com.event.AgentNoAnswerEvent;
import com.event.DomainEvent;
import com.model.Agent;
import com.model.Contact;

import java.util.Optional;
import java.util.UUID;


public class AgentNoAnswerAlarmRule implements AlarmRule {
    @Override
    public Optional<Alarm> evaluate(DomainEvent event) {
        if (event instanceof AgentNoAnswerEvent) {
        AgentNoAnswerEvent noAnswerEvent = (AgentNoAnswerEvent) event;
        
        Agent agent = noAnswerEvent.getAgent();
        Contact contact = noAnswerEvent.getContact();
            return Optional.of(new Alarm(
                    UUID.randomUUID().toString(),
                    AlarmSeverity.WARNING,
                    String.format("%s %s, %s musterisine cevap vermedi (SLA riski)",
                            agent.getName(), agent.getSurname(), contact.getName()),
                    "AgentNoAnswerEvent",
                    noAnswerEvent.occurredAt()
            ));
        }
        return Optional.empty();
    }
}