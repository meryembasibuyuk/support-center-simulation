package com.strategy;

import com.model.Agent;
import java.util.List;

public interface RoutingStrategy {
    Agent route(List<Agent> agents);
}