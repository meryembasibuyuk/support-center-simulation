package com.strategy;

import com.model.Agent;
import com.model.Contact;
import java.util.List;

public interface RoutingStrategy {
    Agent route(Contact contact, List<Agent> agents);
}