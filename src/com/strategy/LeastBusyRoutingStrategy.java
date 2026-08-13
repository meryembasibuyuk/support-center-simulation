package com.strategy;

import com.model.Agent;
import com.model.AgentStatus;
import com.model.Contact;

import java.util.List;

public class LeastBusyRoutingStrategy implements RoutingStrategy {
    @Override
    public Agent route(Contact contact, List<Agent> agents) {
        for (Agent agent : agents) {
            if (agent.getStatus() == AgentStatus.ONLINE) {
                return agent;
            }
        }
        return null;
    }