package com.strategy;

import com.model.Agent;
import com.model.Message;
import java.util.List;

public interface RoutingStrategy {
    Agent route(Message message, List<Agent> availableAgents);
}