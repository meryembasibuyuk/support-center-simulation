package com.strategy;

import com.model.Agent;
import com.model.AgentStatus;
import com.model.Contact;
import java.util.Comparator;
import java.util.List;

public class LeastBusyRoutingStrategy implements RoutingStrategy {

    @Override
    public Agent route(List<Agent> agents, Contact contact, Agent lastInteractedAgent) {
        if (agents == null || contact == null) {
            return null;
        }
        return agents.stream()
                .filter(a -> a.getStatus() == AgentStatus.ONLINE)
                .filter(a -> a.supportsChannel(contact.getChannel()))
                .filter(Agent::hasCapacity)
                .min(Comparator.comparingInt(a -> a.getActiveSessions().size()))
                .orElse(null);
    }
}
