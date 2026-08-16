package com.strategy;

import com.model.Agent;
import com.model.AgentStatus;
import com.model.Contact;
import java.util.List;

public class AdvancedRoutingStrategy implements RoutingStrategy {

    @Override
    public Agent route(List<Agent> agents, Contact contact, Agent lastInteractedAgent) {
        if (agents == null || agents.isEmpty() || contact == null) {
            return null;
        }

        // 1. Sticky Agent: onceki temsilci ONLINE mi, kapasitesi var mi ve kanali destekliyor mu?
        if (lastInteractedAgent != null
                && lastInteractedAgent.getStatus() == AgentStatus.ONLINE
                && lastInteractedAgent.hasCapacity()
                && lastInteractedAgent.supportsChannel(contact.getChannel())) {
            return lastInteractedAgent;
        }

        // 2. Genel yonlendirme: ONLINE, kanali destekleyen ve kapasitesi olan ilk temsilci
        return agents.stream()
                .filter(a -> a.getStatus() == AgentStatus.ONLINE)
                .filter(a -> a.supportsChannel(contact.getChannel()))
                .filter(Agent::hasCapacity)
                .findFirst()
                .orElse(null);
    }
}