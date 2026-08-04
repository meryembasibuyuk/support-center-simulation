package com.strategy;

import com.model.Agent;
import java.util.List;

public class FirstAvailableRoutingStrategy implements RoutingStrategy {
    @Override
    public Agent selectAgent(List<Agent> agents) {
        for (Agent agent : agents) {
            if (!agent.isBusy()) {
                return agent; // Müsait olan ilk temsilciyi döndürür
            }
        }
        return null; // Müsait temsilci yoksa null döner
    }
}