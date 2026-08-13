package com.strategy;

import com.model.Agent;
import com.model.Contact;
import java.util.List;

public interface RoutingStrategy {
    Agent route(List<Agent> agents, Contact contact, Agent lastInteractedAgent);
}