package com.strategy;

import com.model.Agent;
import java.util.List;

public class FirstAvailableRoutingStrategy implements RoutingStrategy {
    @Override
    public Agent route(List<Agent> agents) {
        for (Agent agent : agents) {
            if (agent.isAvailable()) {
                return agent;
            }
        }
        return null;
    }
}