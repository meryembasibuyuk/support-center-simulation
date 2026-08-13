package com.strategy;

import com.model.Agent;
import com.model.AgentStatus;
import com.model.Contact;
import java.util.List;

public class ChannelSpecialistRoutingStrategy implements RoutingStrategy {

    @Override
    public Agent route(List<Agent> agents, Contact contact, Agent lastInteractedAgent) {
        return agents.stream()
                .filter(a -> a.supportsChannel(contact.getChannel()))
                .filter(a -> a.getStatus() == AgentStatus.ONLINE)
                .filter(Agent::hasCapacity)
                .findFirst()
                .orElse(null);
    }
}