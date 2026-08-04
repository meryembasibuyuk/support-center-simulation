package com.strategy;

import com.model.Agent;
import java.util.List;

public class LeastBusyRoutingStrategy implements RoutingStrategy {
    @Override
    public Agent selectAgent(List<Agent> agents) {
        Agent bestAgent = null;
        int minJobs = Integer.MAX_VALUE;

        for (Agent agent : agents) {
            // Sadece müsait olan temsilciler arasından seçim yap
            if (!agent.isBusy()) {
                if (agent.getCompletedJobCount() < minJobs) {
                    minJobs = agent.getCompletedJobCount();
                    bestAgent = agent;
                }
            }
        }
        return bestAgent;
    }
}