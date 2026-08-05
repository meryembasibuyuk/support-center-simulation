package com.strategy;

import com.model.Agent;
import java.util.List;

public class LeastBusyRoutingStrategy implements RoutingStrategy {
    @Override
    public Agent route(List<Agent> agents) {
        Agent leastBusyAgent = null;
        int minWorkload = Integer.MAX_VALUE;

        for (Agent agent : agents) {
            if (agent.isAvailable() && agent.getAssignedMessageCount() < minWorkload) {
                minWorkload = agent.getAssignedMessageCount();
                leastBusyAgent = agent;
            }
        }
        return leastBusyAgent;
    }
}